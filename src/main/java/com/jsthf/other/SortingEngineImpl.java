package com.jsthf.other;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ui.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jsthf.controller.FCExItemController;
import com.jsthf.model.FCExItem;
import com.jsthf.model.Risk;
import com.jsthf.model.User;
import com.jsthf.service.FCExItemService;
import com.jsthf.service.RiskService;
import com.jsthf.service.UserService;

@SpringBootApplication(scanBasePackages = { "com.jsthf.other" })
public class SortingEngineImpl implements SortingEngine {

	@Autowired
	private UserService userService;

	@Autowired
	private FCExItemService fcexitemsService;

	@Autowired
	private RiskService riskService;

	private List<FCExItem> cards;

	private boolean isNewSelection = false;

	private static Logger myLogger = Logger
			.getLogger(FCExItemController.class.getName());

	public SortingEngineImpl() {
	}

	public List<FCExItem> generateCards(User user) {

		cards = new ArrayList<>();
		List<FCExItem> fcards;
		
		// 1. regardless of the sort option the pre-added cards and cards added by the currently logged in user are loaded from DB
		// query used is:
		// @Query(nativeQuery = true, value="SELECT * FROM fcex_item c WHERE c.user = :Name OR c.user = 'adm'")
		// List<FCExItem> findAllCardsAddedByUserAndAdmin(@Param("Name") String Name);

		// 3. sort if user wants only his/her cards
		
		if (user.getOnlymycards()==1) {
			fcards = fcexitemsService.findAllCardsAddedByUser(user.getUsername());
		} else {
			fcards = fcexitemsService.findAllCardsAddedByUserAndAdmin(user.getUsername());
		}
		
		// start sorting if ...
		if (fcards.size() > 0) {

			// 2. regardless of sort option the cards pertaining to selected framework are selected
			if (user.getOnlymyframeworks() == 0) {   // 0 is default value. By default cards in the selected frameworks only are selected
				
				// to avoid repetition of the same card (since same card may be under two or more frameworks) Set is used
				Set<FCExItem> cardsTmp = new LinkedHashSet<>();
				
				String fmu = user.getFrameworkSeln().toString();
				fmu = ","+fmu.toLowerCase().replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\s", "")+",";
				
				for (FCExItem fc : fcards) {				
					List<String> fms = fc.getFrameworkSeln();
					
					for (String fm : fms) {
						fm = ","+fm+",";
						if (fmu.contains(fm)) cardsTmp.add(fc);
					}
				}
	
				fcards = new ArrayList(cardsTmp);
			}

			
			// 3. further sort according to loadType option 
			int loadType = Integer.parseInt(user.getLoadTypeSeln());
			int cardType = Integer.parseInt(user.getCardTypeSeln());
			int sortType = Integer.parseInt(user.getRandomNotSeln());

			// adds all cards - sorts by card type based on selection criteria or all if both types are selected
			if (loadType == 0) {

				// using Java legacy code I would do it as follows:
				// if (cardType==0) for (FCExItem fc: fcards) if
				// (fc.getType().equals("0")) cards.add(fc);

				// same thing using Java Stream
				if (cardType == 0)
					fcards.stream().filter(n -> n.getType().equals("0"))
							.forEach(n -> cards.add(n));

				if (cardType == 1)
					fcards.stream().filter(n -> n.getType().equals("1"))
							.forEach(n -> cards.add(n));

				if (cardType == 2)
					cards = fcards;

			// searches by a keyword
			} else if (loadType == 1) {

				String keyword = user.getKeywordSeln();

				// using Java legacy code I would do it as follows:
				// if (cardType==0) for (FCExItem fc: fcards) if
				// (fc.getType().equals("0") && fc.getFirst().contains(keyword)
				// ||
				// fc.getType().equals("0") && fc.getSecond().contains(keyword))
				// cards.add(fc);

				// same thing using Java Stream
				if (cardType == 0 || cardType == 2)
					fcards.stream().filter(n -> n.getType().equals("0"))
							.filter((n) -> n.getFirst().contains(keyword)
									|| n.getSecond().contains(keyword)
									|| n.getTag().contains(keyword))
							.forEach(n -> cards.add(n));

				if (cardType == 1 || cardType == 2)
					fcards.stream().filter(n -> n.getType().equals("1"))
							.filter((n) -> n.getFirst().contains(keyword)
									|| n.getSecond().contains(keyword)
									|| n.getTag().contains(keyword))
							.forEach(n -> cards.add(n));

			// sorts all cards whose status is 'Never repeat'
			} else if (loadType == 2) {

				String nev = "";
				if (userService.existsByName(user.getUsername())) {
					long currentUserId = userService.idByName(user.getUsername());
					nev = userService.findById(currentUserId).getNeversSeln();
				}
				
				if (nev != null && nev.length()>0) {
					List<String> nevs = Arrays.asList(nev.split(","));	

					for (String nr : nevs) {
	
						String nv = nr.trim();
	
						if (cardType == 0 || cardType == 2)
							fcards.stream().filter(n -> n.getType().equals("0"))
									.filter(n -> n.getId().toString().equals(nv))
									.forEach(n -> cards.add(n));
	
						if (cardType == 1 || cardType == 2)
							fcards.stream().filter(n -> n.getType().equals("1"))
									.filter(n -> n.getId().toString().equals(nv))
									.forEach(n -> cards.add(n));
					}
				}
			// sorts all those which do not have a tag
			} else if (loadType == 3) {

				// using Java legacy code I would do it as follows:
				// if (cardType==0) for (FCExItem fc: fcards) if
				// (fc.getType().equals("0") && fc.getTag() == null ||
				// fc.getType().equals("0") && fc.getTag().length()==0)
				// cards.add(fc);

				if (cardType == 0 || cardType == 2)
					fcards.stream().filter(n -> n.getType().equals("0"))
							.filter((n) -> n.getTag() == null
									|| n.getTag().length() == 0)
							.forEach(n -> cards.add(n));

				if (cardType == 1 || cardType == 2)
					fcards.stream().filter(n -> n.getType().equals("1"))
							.filter((n) -> n.getTag() == null
									|| n.getTag().length() == 0)
							.forEach(n -> cards.add(n));

			// sorts by tags - most complex sorting
			} else if (loadType == 4) {

				boolean isAlreadyAssigned = false;

				List<String> tcs = user.getTagsSeln();

				Set<FCExItem> sortedCards = new LinkedHashSet<>();
				Set<FCExItem> tempCards = new LinkedHashSet<>();

				for (String tg : tcs) {
					tg = tg.replaceAll("\\s", "");
					// if rule is AND
					if (user.getTagsRule() != null
							&& user.getTagsRule().equals("0")) {
						if (isAlreadyAssigned) {
							tempCards = sortedCards;
							sortedCards = new LinkedHashSet<FCExItem>();
						} else {
							tempCards = new LinkedHashSet<FCExItem>(fcards);
							isAlreadyAssigned = true;
						}
					}
					// if rule is OR or no rule is set (because only one tag was
					// selected)
					if (user.getTagsRule() == null || user.getTagsRule() != null
							&& user.getTagsRule().equals("1"))
						tempCards = new LinkedHashSet<FCExItem>(fcards);

					// if match rule is EXACT comm = ",", if PARTIAL comm = "";
					String comm;
					if (user.getTagsMatch().equals("0")) {
						comm = ",";
					} else {
						comm = "";
					}
					for (FCExItem fc : tempCards) {
						// commas are added on both sides to capture tags at the
						// beginning and at the end
						// and achieve exact match while enabling to avoid
						// iteration
						String cardtags = comm
								+ fc.getTag().replaceAll("\\s", "") + comm;
						for (int i = 0; i < 2; i++) {
							String nm = "" + i;
							if (cardType == i || cardType == 2)
								if (fc.getType().equals(nm)
										&& cardtags.contains(comm + tg + comm))
									sortedCards.add(fc);
						}
					}
				}
				cards = new ArrayList<FCExItem>(sortedCards);
			
			// sorts by topics
			} else if (loadType == 5) {

				// to avoid repetition of the same card (since same card may be under two or more topics) Set is used
				Set<FCExItem> cardsTmp = new LinkedHashSet<>();
				
				String fmu = user.getTopicsSeln().toString();
				fmu = ","+fmu.toLowerCase().replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\s", "")+",";

				for (FCExItem fc : fcards) {				
					List<String> fms = fc.getTopic();

					boolean is2add = false;
					if (cardType == 0  && fc.getType().equals("0") || cardType == 2 && fc.getType().equals("0")) is2add = true;
					if (cardType == 1  && fc.getType().equals("1") || cardType == 2 && fc.getType().equals("1")) is2add = true;
					for (String fm : fms) {
						fm = ","+fm+",";
						if (is2add && fmu.contains(fm)) {
							cardsTmp.add(fc);
						}
					}
				}
	
				cards = new ArrayList(cardsTmp);

			// sorts by risks
			} else if (loadType == 6) {

				List<String> rsks = user.getRisksSeln();
				//myLogger.info("===========>>>>>>>>>>> in  Engine rsks size = " + rsks.size()+" 0= '"+rsks.get(0)+"'");
				if (rsks.size()>0) {
					for (String rk : rsks) {
						String rsk = rk.trim();
						//myLogger.info("===========>>>>>>>>>>> in  Engine rsk = " + rsk);
	
						if (cardType == 0 || cardType == 2)
							fcards.stream().filter(n -> n.getType().equals("0"))
									.filter(n -> n.getRisk().getId().toString().equals(rsk))
									.forEach(n -> cards.add(n));
	
						if (cardType == 1 || cardType == 2)
							fcards.stream().filter(n -> n.getType().equals("1"))
									.filter(n -> n.getRisk().getId().toString().equals(rsk))
									.forEach(n -> cards.add(n));
					}
				}
			}

			// removes all cards selected by user as 'Never repeat' cards
			if (loadType != 2) {
				List<FCExItem> cards2 = new ArrayList<>();
				String nev = "," + user.getNeversSeln() + ",";
				for (FCExItem ac : cards) {
					String idc = "," + ac.getId() + ",";
					if (!nev.contains(idc))
						cards2.add(ac);
				}
				cards = cards2;
			}

			// change order based on user selected criteria
			if (cards.size() > 0) {
				if (sortType == 0) {
					Collections.shuffle(cards);
					// since cards fetched from DB are already sorted in asc.
					// order do nothing if sortType == 1
				} else if (sortType == 2) {
					Collections.reverse(cards);
				}
			}

		}

		return cards;
	}

	// returns list of all tags that any card in the collection contains
	// shared / used by many methods in the FCExItemController and UserController
	
	public List<String> getTags(String currentUser) {
		Set<String> tags = new TreeSet<String>();
		List<FCExItem> fcards = fcexitemsService.findAllCardsAddedByUserAndAdmin(currentUser);
		for (FCExItem card : fcards) {
			if (card.getTag() != null) {
				List<String> tagsSepd = Arrays.asList(card.getTag().split(","));
				tagsSepd.stream().map(a -> a.trim()).filter(b -> b.length() > 0)
						.forEach(a -> tags.add(a));
			}
		}

		return new ArrayList(tags);
	}

	public void setIsNewSelection(boolean isNewS) {
		this.isNewSelection = isNewS;
	}

	public boolean isNewSelection() {
		return isNewSelection;
	}

	// following 3 methods convert to and return values in JSON format
	// shared / used by many methods in the FCExItemController and RiskController 
	@Override
	public String getCardsInJSONformat(List<FCExItem> fcs) {
		List collection = new ArrayList();
		for (FCExItem fc : fcs) {
			String framework = fc.getFrameworkSeln().toString();
			framework = framework.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\s", "");
			collection.add(new Cards(fc.getId(), fc.getFirst(), fc.getSecond(),
					fc.getType(), fc.getTag(), fc.getUser(), framework));
		}

		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(collection);
	}
	
	@Override
	public String getFrameworksInJSONformat() {

		List<FMWorks> collection = new ArrayList();
		for (Framework fc : Framework.values()) {
			collection.add(new FMWorks(fc.id, fc.name));
		}
	
		Gson gson = new Gson();
		return gson.toJson(collection);
	}


	@Override
	public String getTopicsInJSONformat() {

		List<Tops> collection = new ArrayList();
		for (Topic fc : Topic.values()) {
			collection.add(new Tops(fc.id, fc.name, fc.framework));
		}
	
		Gson gson = new Gson();
		return gson.toJson(collection);
	}

	@Override
	public String getRisksInJSONformat(String username) {

		List<RiskRep> risksJsn = new ArrayList();
		for (Risk fc : riskService.findAllRisksAddedByUserAndAdmin(username)) {
			risksJsn.add(new RiskRep(fc.getId(), fc.getName(), fc.getTopic().toString(), fc.getFrameworkId(), fc.getUser()));
		}
	
		Gson gson = new Gson();
		return gson.toJson(risksJsn);
	}
	
	// following 2 methods are used to partially "clone" class objects so that 
	// users did not have to fill all form fields over and over again.
	// used by few methods in the FCExItemController and RiskController  
	@Override
	public Model addPresetCard(Model model, String currentUser) {
		
		List<FCExItem> fcs = fcexitemsService.findAllCardsAddedByUser(currentUser);
		boolean isAnyByCurrentUser = false;
		int lastId = -1;
		
		if (fcs.size() > 0) {
			isAnyByCurrentUser = true;
			lastId = fcs.size() - 1;
		}

		if (!model.containsAttribute("fcexitem")) {
			FCExItem nCard = new FCExItem();
			nCard.setUser(currentUser);
			if (isAnyByCurrentUser) {
				FCExItem exCard = fcs.get(lastId);
				nCard.setTag(exCard.getTag());
				nCard.setType(exCard.getType());
				nCard.setTopic(exCard.getTopic());
				nCard.setFrameworkSeln(exCard.getFrameworkSeln());
				nCard.setRisk(exCard.getRisk());
			}
			model.addAttribute("fcexitem", nCard);
		}
		
		List<Risk> risks = riskService.findAllRisksAddedByUserAndAdmin(currentUser);
		if (risks.size() == 0) {
			Risk risk = new Risk();
			risk.setId(1L);
			risk.setName("no risk");
			risk.setUser("adm");
			risk.setTopic(Arrays.asList("0"));
			riskService.save(risk);
		}
		return model;
	}
	
	
	@Override
	public Model addPresetRisk(Model model, String currentUser) {

		List<Risk> rss = riskService.findAllRisksAddedByUser(currentUser);

		boolean isAnyByCurrentUser = false;
		int lastId = -1;
		if (rss.size() > 0) {
			isAnyByCurrentUser = true;
			lastId = rss.size() - 1;
		}

		if (!model.containsAttribute("risk")) {
			Risk nRisk = new Risk();
			nRisk.setUser(currentUser);
			if (isAnyByCurrentUser) {
				Risk exRisk = rss.get(lastId);
				nRisk.setTopic(exRisk.getTopic());
				nRisk.setFrameworkId(exRisk.getFrameworkId());
				nRisk.setTopic(exRisk.getTopic());
			} 
			model.addAttribute("risk", nRisk);
		}

		return model;
	}
	
	// shared / used by few methods in the FCExItemController and RiskController
	@Override
	public Model addFramework(Model model, String currentUser) {

		String userFramework = "";
		if (userService.existsByName(currentUser)) {
			long currentUserId = userService.idByName(currentUser);
			int userFrameworkId = userService.findById(currentUserId).getFramework();
			userFramework = Framework.values()[userFrameworkId].toString();
		}

		model.addAttribute("userframework", userFramework);
		
		return model;
	}
	
	// returns source code of all files in this app - used by 'Source code' option in the main menu
	@Override
	public String getSourceCodeText() {
		
			List<Path> filesList = new ArrayList<Path>();

			// TODO: update when publishing
			// TODO: when publishing check that Mac paths work in host server
			String base = "/Users/myfamily/eclipse-workspace/JSTHF";
			
			List<Cods> collection = new ArrayList<>();


			// reads names of all files in folder
			try (Stream<Path> paths = Files.walk(Paths.get("/Users/myfamily/eclipse-workspace/JSTHF/src"))) {
			    paths
			        .filter(Files::isRegularFile)
			        .forEach(a -> filesList.add(a));
			} catch (IOException e) {
		        e.printStackTrace();
		    }

			int i = 0; 
			for (Path apath :  filesList) {
				
				String filePath = apath.toString();
				
				String relative = new File(base).toURI().relativize(new File(filePath).toURI()).getPath();

				boolean is2add = true;
				
				String fname = apath.getFileName().toString();

				if (fname.startsWith(".")) is2add = false;
				if (fname.endsWith(".jpg") || fname.endsWith(".png") || fname.endsWith(".ico")) is2add = false;
				if (fname.endsWith(".txt")) is2add = false;
				if (relative.contains("/images/")) is2add = false;
				if (relative.contains("/prettify2/")) is2add = false;
				if (relative.contains("/menulibsonly/")) is2add = false;
				if (relative.contains("/admin/")) is2add = false;
					
				if (is2add) { 
					//myLogger.info("===========>>>>>>>>>>> in  user. i = "+i+" relative = " + relative );
					
					StringBuilder contentBuilder = new StringBuilder();
				    try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
				    {
				        stream.forEach(s -> contentBuilder.append(s).append("\n"));
				    }
				    catch (IOException e)
				    {
				        e.printStackTrace();
				    }
				    String result = contentBuilder.append("\n").append("\n").toString();
				    
				    // escape html special characters
				    	result = result.replaceAll("\\<", "&#60;").replaceAll("\\>","&#62;").replaceAll("\\&", "&amp;");
				    
				    // mask JDBC username and password
				    int ix1 = 26 + result.indexOf("spring.datasource.username"); 
				    int ix2 = result.indexOf("spring.datasource.password");
				    		    
				    if ( ix1 > 0 && ix2 > 0 && ix2 > ix1 ) {
				    		String result2a = result.substring(0, ix1+1) + "*********\n"+ result.substring(ix2, result.length());
				    		result = result2a;
				    }
				    
				    ix1 = 26 + result.indexOf("spring.datasource.password");
				    ix2 = result.indexOf("spring.session.store-type");

				    if ( ix1 > 0 && ix2 > 0 && ix2 > ix1 ) {
				    		String result2b = result.substring(0, ix1+1) + "*********\n"+ result.substring(ix2, result.length());
				    		result = result2b;
				    }
				    
				    collection.add(new Cods(i, fname, relative, result));
				    i++;
				    
				}
			}

		    //myLogger.info("===========>>>>>>>>>>> in  user. CodeResult = "+collection.size());
		    
			Gson gson = new Gson();
			return gson.toJson(collection);
	}
	
	@Override
	public String getSourceCodeFromFile(String thePath) {
		StringBuilder contentBuilder = new StringBuilder();
		try {
			File file = new ClassPathResource(thePath).getFile();
			String path = file.toURI().getPath();
			Stream<String> stream = Files.lines( Paths.get(path), StandardCharsets.UTF_8);
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return contentBuilder.toString();
	}
	

}

//  only constructors of the following five classes are used, getters and setters are not used

class Cards {
	private long id;
	private String first;
	private String second;
	private String type;
	private String tag;
	private String user;
	private String framework;

	public Cards(long id, String first, String second, String type, String tag, String user, String framework) {
		this.id = id;
		this.first = first;
		this.second = second;
		this.framework = framework;
		this.type = type;
		this.tag = tag;
		this.user = user;
	}
}

class RiskRep {
	private long id;
	private String name;
	private String topic;
	private int frameworkId;
	private String user;

	public RiskRep(long id, String name, String topic, int frameworkId, String user) {
		this.id = id;
		this.name = name;
		this.topic = topic;
		this.frameworkId = frameworkId;
		this.user = user;
	}
}

class FMWorks {
	private long id;
	private String name;

	public FMWorks(long id, String name) {
		this.id = id;
		this.name = name;
	}
}

class Tops {
	private long id;
	private String name;
	private String framework;

	public Tops(long id, String name, String framework) {
		this.id = id;
		this.name = name;
		this.framework = framework;
	}
}

class Cods {
	private long id;
	private String name;
	private String parent;
	private String content;

	public Cods(long id, String name, String parent, String content) {
		this.id = id;
		this.name = name;
		this.parent = parent;
		this.content = content;
	}
}
