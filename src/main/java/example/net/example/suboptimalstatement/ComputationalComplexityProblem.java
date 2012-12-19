package net.example.suboptimalstatement;

/*
 * Wrong occurrences: 1;
 */
public class ComputationalComplexityProblem {
	public void squareOfNComputation() {
		int[] nums = { 3, 1, 2 };
		int sum = 0;
		for (int i : nums){
			for (int j : nums) {
				sum += i * j;
			}
		}
		System.out.println(sum);
	}
}
