package net.example.suboptimalstatement;

/*
 * Wrong occurrences: 4;
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

	public void methodC(){
		String[] keys = { "A", "B", "C", "D" };
		for (String key: keys) {
			DBManager abc=new DBManager();
			abc.someCombinativeFunction2(key);
		}
	}
	
	private void processRow(
			CallCombinativeFunctionTwiceOrMoreInLoop callCombinativeFunctionTwiceOrMoreInLoop) {
		// TODO Auto-generated method stub
		
	}
}
