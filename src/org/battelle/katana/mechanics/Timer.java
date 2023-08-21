package org.battelle.katana.mechanics;
public class Timer {

	private long startTime = 0;
	private long stopTime = 0;
	private boolean running = false;
	private long pauseTime = 0;
	private long subStartTime = 0;
	private long comboTimer = 0;

	/**
	 * Start the timer
	 */
	public void start() {
		this.startTime = System.currentTimeMillis();
		this.running = true;
	}

	/**
	 * Stop the timer
	 */
	public void stop() {
		this.stopTime = System.currentTimeMillis();
		this.running = false;
	}

	/**
	 * Pause the timer. Like stopping except on resume it acts as if there was
	 * never any stop.
	 */
	public void pause() {
		this.pauseTime = System.currentTimeMillis();
		this.stopTime = System.currentTimeMillis();
		this.running = false;
	}

	/**
	 * Wakes the timer up from a paused state.
	 */
	public void resume() {
		this.startTime = this.startTime
				+ (System.currentTimeMillis() - this.pauseTime);
		this.pauseTime = 0;
		this.running = true;
	}

	public void subTimerStart() {
		this.subStartTime = System.currentTimeMillis();
	}

	public long getSubTimerStart() {
		return this.subStartTime;
	}

	public void subTimerEnd() {
		this.subStartTime = 0;
	}

	public void comboTimerStart() {
		this.comboTimer = System.currentTimeMillis();
	}

	public void comboTimerStop() {
		this.comboTimer = 0;
	}

	public long getComboTimer() {
		return this.comboTimer;
	}

	/**
	 * Gets the pauseTime of the timer
	 */
	public long getPause() {
		return this.pauseTime;
	}

	/**
	 * Gets the elapsed time in milliseconds
	 * 
	 * @return The elapsed time in milliseconds
	 */
	public long getElapsedTime() {
		long elapsed;
		if (running) {
			elapsed = (System.currentTimeMillis() - startTime);
		}
		else {
			elapsed = (stopTime - startTime);
		}
		return elapsed % 1000;
	}

	/**
	 * Gets the elapsed time in seconds
	 * 
	 * @return The elapsed time in seconds
	 */
	public long getElapsedTimeSecs() {
		long elapsed;
		if (running) {
			elapsed = ((System.currentTimeMillis() - startTime) / 1000);
		}
		else {
			elapsed = ((stopTime - startTime) / 1000);
		}
		return elapsed;
	}
}