package net.example.suboptimalstatement;

/*
 * Wrong occurrences: 1;
 */
public class CallCombinativeFunctionTwiceOrMoreInLoop {
	public void methodA() {
		String[] keys = { "A", "B", "C", "D" };
		for (String key: keys) {
			CallCombinativeFunctionTwiceOrMoreInLoop callCombinativeFunctionTwiceOrMoreInLoop = DBManager.getRowByID(key);
			processRow(callCombinativeFunctionTwiceOrMoreInLoop);
		}
	}
	
	public void methodB(){
		String[] keys = { "A", "B", "C", "D" };
		for (String key: keys) {
			DBManager.someCombinativeFunction(key);
		}
	}

	private void processRow(
			CallCombinativeFunctionTwiceOrMoreInLoop callCombinativeFunctionTwiceOrMoreInLoop) {
		// TODO Auto-generated method stub
		
	}
}
