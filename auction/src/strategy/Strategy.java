package strategy;

import logist.task.Task;
import planner.GeneralPlan;
import planner.PlannerTrait;
import bidder.BidStrategyTrait;
import estimator.CostEstimatorTrait;

public class Strategy {
	private PlannerTrait planner;
	private final CostEstimatorTrait estimator;
	private final BidStrategyTrait bidder;
	private Task currentTask;

	public Strategy(PlannerTrait planner, CostEstimatorTrait estimator, BidStrategyTrait bidder) {
		this.planner = planner;
		this.estimator = estimator;
		this.bidder = bidder;
	}

	public Long bid(Task task) {
		currentTask = task;
		return bidder.bid(estimator.computeMC(planner, currentTask));
	}

	public void validateBid(Boolean won, Long[] lastOffers) {
		if (won) {
			planner = planner.extendPlan(currentTask);
		}

		bidder.addBids(lastOffers);
	}

	public GeneralPlan generatePlans() {
		return planner.generatePlans();
	}
}
