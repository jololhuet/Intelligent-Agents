package estimator;

import static utils.Utils.ensure;
import logist.task.DefaultTaskDistribution;
import logist.task.Task;
import logist.task.TaskDistribution;
import planner.PlannerTrait;

public class Gipsy extends NoFuture {

	private final int minTasks;
	private final int nbPredictions;
	private final double riskTolerance;
	private final DefaultTaskDistribution distribution;

	public Gipsy(int minTasks, int nbPredictions, double riskTolerance, TaskDistribution distribution) {
		this.minTasks = minTasks;
		this.nbPredictions = nbPredictions;
		this.riskTolerance = riskTolerance;
		this.distribution = (DefaultTaskDistribution) distribution;

		ensure(minTasks >= 0, "invalid minimun number of tasks");
		ensure(nbPredictions >= 1, "invalid number of predications");
		ensure(riskTolerance >= 0 && riskTolerance <= 1, "invalid risk tolerance");
	}

	@Override
	public double computeMC(PlannerTrait planner, Task task) {
		double normalMC = super.computeMC(planner, task); // use NoFuture

		if (planner.tasks.size() >= minTasks || normalMC == 0)
			return normalMC;

		// Compute a few estimation
		double worsePrediction = Double.NEGATIVE_INFINITY;
		double bestPrediction = Double.POSITIVE_INFINITY;
		double sum = 0;
		for (int i = 0; i < nbPredictions; ++i) {
			PlannerTrait vision = planner;

			// Extend the current planner with random tasks
			while (vision.tasks.size() < minTasks) {
				vision = vision.extendPlan(createTask());
			}

			double prediction = super.computeMC(vision, task);
			worsePrediction = Math.max(worsePrediction, prediction);
			bestPrediction = Math.min(bestPrediction, prediction);

			sum += prediction;
		}

		double prediction = Math.min(worsePrediction, normalMC);
		prediction = normalMC - (normalMC - prediction) * riskTolerance;

		// TODO remove me one day
		double avg = sum / nbPredictions;
		System.out.println("Gipsy " + planner.tasks.size() + " " + minTasks + " " + nbPredictions);
		System.out.println("Gipsy WORSE  " + worsePrediction);
		System.out.println("Gipsy BEST   " + bestPrediction);
		System.out.println("Gipsy AVG    " + avg);
		System.out.println("Gipsy NORMAL " + normalMC);
		System.out.println("Gipsy PRED   " + prediction);

		return prediction;
	}

	/**
	 * Generate a random task
	 */
	private Task createTask() {
		return distribution.createTask();
	}

}
