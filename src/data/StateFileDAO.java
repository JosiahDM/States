package data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

public class StateFileDAO implements StateDAO {
	private static final String FILE_NAME="/WEB-INF/states.csv";
	private List<State> states = new ArrayList<>();
	private List<State> filteredStates = new ArrayList<>();
	
	/*
	 * Use Autowired to have Spring inject an instance
	 * of a WebApplicationContext into this object after
	 * creation.  We will use the WebApplicationContext to
	 * retrieve an ServletContext so we can read from a 
	 * file.
	 */
	@Autowired 
	private WebApplicationContext wac;

	/*
	 * The @PostConstruct method is called by Spring after 
	 * object creation and dependency injection
	 */
	@PostConstruct
	public void init() {
		// Retrieve an input stream from the servlet context
		// rather than directly from the file system
		try (
				InputStream is = wac.getServletContext().getResourceAsStream(FILE_NAME);
				BufferedReader buf = new BufferedReader(new InputStreamReader(is));
			) {
			String line = buf.readLine();
			while ((line = buf.readLine()) != null) {
				String[] tokens = line.split(",");
				int id = Integer.parseInt(tokens[0]);
				String abbrev = tokens[1];
				String name = tokens[2];
				String capital = tokens[3];
				String latitude = tokens[4];
				String longitude = tokens[5];
				String population = tokens[6];
				states.add(new State(id, abbrev, name, capital, latitude, longitude, population));
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		filteredStates.addAll(states);
	}

	@Override
	public void addState(State state) {
		states.add(state);
	}

	@Override
	public void applyFilter(Predicate<State> predicate) {
		filteredStates.clear();
		for(State state : states) {
			if(predicate.test(state)) {
				filteredStates.add(state);
System.out.println("adding..." + state.getName());
			}
		}
	}
	
	@Override
	public void clearFilter() {
		filteredStates.addAll(states);
	}
	
	
	@Override
	public State getOne(Predicate<State> predicate) {
		State out = null;
		for(State state : states) {
			if(predicate.test(state)) {
				out = state;
			}
		}
		return out;
	}
	
	// Go forward or backwards one state depending on direction.
	// 0 or less for backwards, greater than 0 for forward
	public State change(int direction, State currentState) {
		State state = null;
		int index = filteredStates.indexOf(currentState);
		int last = filteredStates.size() - 1;
		if(direction <= 0) {
			state = (index > 0) ? filteredStates.get(index-1) : filteredStates.get(last);
		} else {
			state = (index < last) ? filteredStates.get(index+1) : filteredStates.get(0);
		}
		return state;
	}
	
	public boolean hasStates() {
		return filteredStates.size() > 0;
	}
	
	public State getFirst() {
		return filteredStates.get(0);
	}
	
	// debug method
	public List<State> getStates() {
		return filteredStates;
	}
}

