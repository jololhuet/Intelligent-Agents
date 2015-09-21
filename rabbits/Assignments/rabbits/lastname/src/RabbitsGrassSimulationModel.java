import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.gui.DisplaySurface;

/**
 * Class that implements the simulation model for the rabbits grass simulation.
 * This is the first class which needs to be setup in order to run Repast
 * simulation. It manages the entire RePast environment and the simulation.
 *
 * @author
 */

public class RabbitsGrassSimulationModel extends SimModelImpl {

	/**
	 * Prepares the model for a new run
	 */
	@Override
	public void setup() {
		// clean everything in fact
		space = null;
		surface = null;
		schedule = null;
	}

	/**
	 * Gets the names of the initial model parameters to set.
	 */
	@Override
	public String[] getInitParam() {
		String[] initParams = { "GridSize", "GrassGrowthRate" };
		return initParams;
	}

	/**
	 * Begins a simulation run.
	 */
	@Override
	public void begin() {
		buildModel();
		buildSchedule();
		buildDisplay();

		// Open the display window 
		surface.display();
	}

	@Override
	public String getName() {
		return "Killer Rabbit of Caerbannog";
	}

	@Override
	public Schedule getSchedule() {
		return schedule;
	}

	public int getGridSize() {
		return gridSize;
	}

	public void setGridSize(int size) {
		gridSize = size;
	}

	public int getGrassGrowthRate() {
		return grassGrowthRate;
	}

	public void setGrassGrowthRate(int rate) {
		grassGrowthRate = rate;
	}

	private void buildModel() {
		space = new RabbitsGrassSimulationSpace(getGridSize());
	}

	private void buildSchedule() {
		schedule = new Schedule();

		// Grow the grass at every clock tick
		schedule.scheduleActionAtInterval(1, new BasicAction() {
			@Override
			public void execute() {
				space.growGrass(grassGrowthRate);
			}
		});
		
		// Repaint the surface frequently
		schedule.scheduleActionAtInterval(1, new BasicAction() {
			@Override
			public void execute() {
				surface.updateDisplay();
			}
		});
	}

	private void buildDisplay() {
		surface = new DisplaySurface(space.getDimension(), this, "Display");
		surface.addDisplayable(space.getGrassDisplayable(), "Grass");
		registerDisplaySurface("Grass", surface);
	}

	// Our even scheduler
	private Schedule schedule;

	// Our space representation
	private RabbitsGrassSimulationSpace space;

	// 2D surface for rendering
	private DisplaySurface surface;

	// Simulation parameters
	private int gridSize = DEFAULT_GRID_SIZE;
	private int grassGrowthRate = DEFAULT_GRASS_GROWTH_RATE;

	// Default values for parameters
	static private final int DEFAULT_GRID_SIZE = 20;
	static private final int DEFAULT_GRASS_GROWTH_RATE = 1;
}
