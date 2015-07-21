package vekaUtils;

import android.os.Handler;
import android.os.Looper;

public abstract class Interval {

	final private Handler handler = new Handler(Looper.getMainLooper());	
	final private static int defaultFrequence = 1000;
	
	private int frequence = defaultFrequence;
	private boolean serviceStates = false;
	private int nbOfLoop = 0;
	private int countLoop = 0;
	
	/**
	 * Create an interval with a custom frequence and number of loop
	 * @param interval int frequence in ms
	 * @param nbOfLoop int number of loop
	 */
	public Interval(int interval, int nbOfLoop){
		setInterval(interval);
		setNbOfLoop(nbOfLoop);
	}
	
	/**
	 * Create an interval with a custom frequence
	 * @param interval int frequence in ms
	 */
	public Interval(int interval){
		setInterval(interval);
	}
	
	public Interval(){
		this(defaultFrequence);
	}
	
	/**
	 * get the current interval in ms
	 * @return
	 */
	public int getInterval() {
		return frequence;
	}

	/**
	 * get the current interval in ms
	 * @param interval in ms
	 * @return
	 * 
	 */
	public void setInterval(int interval) {
		this.frequence = interval;
	}

	/**
	 * Get the limit of loop
	 * @return int
	 */
	public int getNbOfLoop() {
		return nbOfLoop;
	}

	/**
	 * Set the limit of loop.
	 * <br/>
	 * default = 0 = unlimited loop
	 * @param nbOfLoop int
	 */
	public void setNbOfLoop(int nbOfLoop) {
		
		if(nbOfLoop < 0)
			nbOfLoop = 0;
		
		this.nbOfLoop = nbOfLoop;
	}

	/**
	 * get the current number of loop 
	 * @return
	 */
	public int getCountLoop() {
		return countLoop;
	}

	/**
	 * start the interval
	 */
	public boolean start(){
		if(serviceStates)
			return false;
		serviceStates = true;		
		return handler.postDelayed(runnable, frequence);
	}
	
	/**
	 * stop the interval 
	 */
	public void stop() {
		if(!serviceStates)
			return ;

		serviceStates = false;
	}
	
	private void initStart() {
		run();
	}
	
	/**
	 * Runnable thread in interval
	 */
	final private Runnable runnable = new Runnable() {
	   public void run() {
		   if(!serviceStates)
			   return;
		   
		   countLoop++;
		   initStart();

		   if((nbOfLoop != 0) && (countLoop >= nbOfLoop)){
			   stop();
			   return;
		   }
		   		   
	       handler.postDelayed(runnable, frequence);
	   }
	};

	/**
	 * you should overwrite this method for define some code to execute
	 */
	public abstract void run();

	
	
	
}
