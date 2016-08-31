package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import data.State;
import data.StateDAO;

@Controller
@SessionAttributes("stateId")
public class StateController {
	@Autowired
	private StateDAO stateDao;
	
	@ModelAttribute("stateId")
	public int initStateId() {
		return 1;
	}

	@RequestMapping(path="GetStateData.do", 
			params="name",
			method=RequestMethod.GET)
	public ModelAndView getByName(@RequestParam("name") String n) {
		ModelAndView mv = new ModelAndView("result.jsp");
		stateDao.clearFilter();
		State state = stateDao.getOne(s -> s.getName().equalsIgnoreCase(n));
		mv.addObject("state", state);
		if(state != null) {
			mv.addObject("stateId", state.getId());
		}
		return mv;
	}
	
	@RequestMapping(path="GetStateData.do", 
			params="abbr",
			method=RequestMethod.GET)
	public ModelAndView getByAbbrev(@RequestParam("abbr") String a) {
		ModelAndView mv = new ModelAndView("result.jsp");
		stateDao.clearFilter();
		State state = stateDao.getOne(s -> s.getAbbreviation().equalsIgnoreCase(a));
		mv.addObject("state", state);
		if (state != null) { 
			mv.addObject("stateId", state.getId()); 
		}
		return mv;
	}

	@RequestMapping(path="SearchState.do",
			method=RequestMethod.POST)
	public ModelAndView searchState(@RequestParam("userInput") String input,
				@RequestParam("filter") String choice) {
		ModelAndView mv = new ModelAndView("result.jsp");
		stateDao.clearFilter();
		System.out.println(choice);
		System.out.println(input);
		switch (choice) {
		case "popGreater": // this one works
			stateDao.applyFilter(s -> Integer.parseInt(s.getPopulation()) > parsePop(input, ">"));
			break;
		case "popLess": // working
			stateDao.applyFilter(s -> Integer.parseInt(s.getPopulation()) < parsePop(input, "<"));
			break;
		case "nameContains": // this one works
			stateDao.applyFilter(s -> s.getName().contains(input));
			break;
		case "nameStarts": 
			stateDao.applyFilter(s -> s.getName().charAt(0) == input.toUpperCase().charAt(0));
			break;
		}
		if (stateDao.hasStates()) {
			mv.addObject("state", stateDao.getFirst());			
		}
		return mv;
	}
	
	@RequestMapping(path="GetStateData.do",
			params="next")
	public ModelAndView nextState(@ModelAttribute("stateId") Integer stateId) {
		return stateMv(1, stateId);
	}
	
	@RequestMapping(path="GetStateData.do",
			params="previous") 
	public ModelAndView previousState(@ModelAttribute("stateId") Integer stateId) {
		return stateMv(-1, stateId);
	}
	
	// Returns model and view with one state forward or backwards
	// from current location. Wraps around if at beginning or end.
	private ModelAndView stateMv(int direction, Integer stateId) {
		ModelAndView mv = new ModelAndView("result.jsp");
		State currentState = stateDao.getOne(s -> s.getId() == stateId );
		mv.addObject("state", stateDao.change(direction, currentState));
		mv.addObject("stateId", stateDao.change(direction, currentState).getId());
		return mv;
	}
	
	// returns parsed int value given input. If invalid input, sets value to
	// unobtainable value to ensure app gets 0 results.
	// Direction is used to determine if its greater than or less than.
	private int parsePop(String input, String direction) {
		int popValue = 0;
		try {
			popValue = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			popValue = (direction.equals(">")) ? Integer.MAX_VALUE : -1;
		}
		return popValue;
	}
	
}
