package data;

import java.util.List;
import java.util.function.Predicate;

public interface StateDAO {
	public void addState(State state);
	public static int MAX_ID = 50;
	public static int MIN_ID = 1;
	public State getOne(Predicate<State> predicate);
	public void applyFilter(Predicate<State> predicate);
	public State change(int direction, State currentState);
	public void clearFilter();
	public boolean hasStates();
	public State getFirst();
	public List<State> getStates(); /// debug
}
