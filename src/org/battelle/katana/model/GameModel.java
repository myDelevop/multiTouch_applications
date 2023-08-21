package org.battelle.katana.model;
import java.util.Observable;

import org.battelle.katana.sound.SoundFactory;
import org.battelle.katana.sound.SoundType;
import org.battelle.katana.view.FruitCutInRowObserver;
import org.battelle.katana.view.HeartObserver;
import org.battelle.katana.view.LevelObserver;
import org.battelle.katana.view.ScoreObserver;
import org.battelle.katana.view.TotalFruitObserver;

/**
 * GameModel acts as an observable model for our Observer classes to watch, which in turn will render the view accordingly. Each field in the GameModel
 * corresponds to a visible component on the GameView.
 * 
 * @author CYBER-100
 * 
 */
public class GameModel {
  /** The current score */
  private Score score;
  /** The current number of lives */
  private Hearts hearts;
  /** The total number of fruits cut this game */
  private NumberOfFruitsCut numberOfFruitsCut;
  /** The total number of fruits cut without missing one so far */
  private FruitsCutInRow fruitsCutInRow;
  /** The number of fruits that have been cut in one fell slice */
  private int comboCount;
  private boolean frenzyMode;
  /** Enum value for game type */
  private Mode gameMode;
  /** SoundFactory for playing sounds */
  private SoundFactory soundFactory = SoundFactory.getInstance();
  private Level currentLevel;

  /**
   * Holds the score value to be observed by an implementing observer class.
   * 
   * @author cyber team
   */
  private class Score extends Observable {
    /** Dictates whether or not we are in double score mode */
    private boolean doubleScore = false;
    /** Contains the current value of our score */
    private int scoreValue;
    /** Dictates how much score we need to get a new life */
    private int livesScore;

    /**
     * Default constructor for creating a new Score type.
     * 
     * @param scoreValue The value we wish score to hold
     */
    public Score(int scoreValue) {
      this.scoreValue = scoreValue;
      this.livesScore = 0;
    }

    /**
     * Gets the score value and returns it as an int
     * 
     * @return The current value of our score in int form
     */
    public int getScore() {
      return scoreValue;
    }

    /**
     * Adds the the new score to the current score and checks for the cases of doubleScore mode, and checks if we should add lives as well. This method
     * notifies observers of change.
     * 
     * @param scoreUpdate
     */
    public void setScore(int scoreUpdate) {
      //Update our values with the new score
      this.scoreValue += scoreUpdate;
      this.livesScore += scoreUpdate;

      //Check if we're in double score mode
      if (doubleScore) {
        this.scoreValue += scoreUpdate;
        this.livesScore += scoreUpdate;
      }
      //If we have 1500 or more points accumulated, try to give a new life
      if (this.livesScore >= 10000 && gameMode == Mode.ARCADE) {

        hearts.addHearts();
        hearts.notifyObservers(hearts.heartCount);
        this.livesScore = 0;
      }

      //Set the score as having been changed and notify the observer
      this.setChanged();
      this.notifyObservers(scoreValue);
    }

    /**
     * Sets double score mode to be true or false.
     * 
     * @param set The boolean value we wish double score mode to take.
     */
    public void setDouble(boolean set) {
      this.doubleScore = set;
    }

  }

  /**
   * Holds the level value to be observed by an implementing observer class.
   * 
   * @author cyber team
   */
  private class Level extends Observable {
    /** Our current level we are playing on */
    public int currentLevel;

    /**
     * Default constructor for declaring a new level class.
     * 
     * @param level The level we wish to start at
     */
    public Level(int level) {
      this.currentLevel = level;
    }

    /**
     * Gets the current level in int form from the Level class
     * 
     * @return The current level in int form
     */
    public int getLevel() {
      return this.currentLevel;
    }

    /**
     * Increases the level by one and notifies observers of the change
     */
    public void increaseLevel() {
      this.currentLevel++;
      this.setChanged();
      this.notifyObservers(this.currentLevel);
    }
  }

  /**
   * Holds the current heart value to be observed by an implementing observer class.
   * 
   * @author cyber team
   */
  private class Hearts extends Observable {
    /** The current amount of hearts we have */
    public int heartCount;

    /**
     * Default constructor initializing our heart count to a given value
     * 
     * @param heartCount The number of hearts we wish to start with
     */
    public Hearts(int heartCount) {
      this.heartCount = heartCount;
    }

    /**
     * Gets the number of hearts our class currently models and returns it in int form
     * 
     * @return The number of hearts currently available
     */
    public int getHearts() {
      return this.heartCount;
    }

    /**
     * Adds a heart to the model. This method notifies observers of a changed element
     * 
     * @return True iff we were able to add a heart. False otherwise
     */
    public boolean addHearts() {
      if (this.heartCount < 3) {
        soundFactory.generateSound(SoundType.EXTRALIFE).start();
        this.heartCount++;
        this.setChanged();
        this.notifyObservers(heartCount);
        return true;
      }
      else {
        return false;
      }
    }

    /**
     * Removes a heart from the model. This method notifies observers of a changed element.
     * 
     * @return True iff we were able to remove a heart. False otherwise
     */
    public boolean removeHearts() {
      if (this.heartCount > 0) {
        this.heartCount--;
        this.setChanged();
        this.notifyObservers(-heartCount);
        return true;
      }
      else {
        return false;
      }
    }

  }

  /**
   * Holds the current number of fruits cut to be observed by an implementing observer
   * 
   * @author cyber team
   */
  private class NumberOfFruitsCut extends Observable {
    /** The total number of fruits cut up to this moment */
    private int totalFruitsCut;
    /** The level boost we are requiring to get to the next level, based on the number of fruits cut */
    private int levelBoost;

    /**
     * Default constructor for creating a new observable fruits cut class
     * 
     * @param totalFruitsCut The total number of fruits we wish to start out with having cut
     */
    public NumberOfFruitsCut(int totalFruitsCut) {
      this.totalFruitsCut = totalFruitsCut;
      this.levelBoost = 10;
    }

    /**
     * Updates the number of fruits cut by adding one. Also can increase the level. This method notifies observers of a changed element.
     */
    public void updateFruitsCut() {
      this.totalFruitsCut++;

      //In the event that we are at a proper amount of fruits cut, increase the level
      if (this.totalFruitsCut - this.levelBoost >= 0) {
        //Increase the level every 10, 10, 20, 40, 70, (+0, +10, +20, +30) fruits cut
        this.levelBoost = levelBoost + (10 * currentLevel.getLevel());
        currentLevel.increaseLevel();
      }
      //Set the values to having been changed and notify the observers
      this.setChanged();
      this.notifyObservers(totalFruitsCut);
    }

    /**
     * Gets the number of fruits cut up to this moment.
     * 
     * @return The total number of fruits cut in int form.
     */
    public int getFruitsCut() {
      return this.totalFruitsCut;
    }

  }

  /**
   * Holds the number of fruits we have "combo"'d, or cut in a row without missing a fruit. To be observed by an implementing class
   * 
   * @author cyber team
   * 
   */
  private class FruitsCutInRow extends Observable {
    /** The total number of fruits we have cut in a row to this point */
    private int totalFruitsCutInRow;
    /** The maximum number of fruits we have ever cut this game */
    private int maxFruits;

    /**
     * Default constructor for declaring an initial number of fruits cut to start with
     * 
     * @param totalFruitsCutInRow The number of fruits we wish to start out having cut in a row
     */
    public FruitsCutInRow(int totalFruitsCutInRow) {
      this.totalFruitsCutInRow = totalFruitsCutInRow;
      maxFruits = 0;
    }

    /**
     * Adds one to the total number of fruits cut in a row up to this point. This method notifies observers that an element has been changed.
     */
    public void updateFruitsCutInRow() {
      this.totalFruitsCutInRow++;

      this.setChanged();
      this.notifyObservers(totalFruitsCutInRow);
    }

    /**
     * Resets the number of fruits cut in a row upon missing. This method notifies observers that an element has been changed.
     */
    public void resetFruitsCutInRow() {
      if (totalFruitsCutInRow > maxFruits) {
        maxFruits = totalFruitsCutInRow;
      }
      this.totalFruitsCutInRow = 0;
      this.setChanged();
      this.notifyObservers(totalFruitsCutInRow);

    }

    /**
     * Returns the maximum number of fruit cut in a row that has ever been achieved this game
     * 
     * @return The maximum number of fruit cut in a row achieved this game.
     */
    public int getMax() {
      return this.maxFruits;
    }

  }

  /**
   * Default constructor for initializing the Game Model quickly.
   */
  public GameModel(Mode gameMode) {
    this.score = new Score(0);
    this.hearts = new Hearts(3);
    this.numberOfFruitsCut = new NumberOfFruitsCut(0);
    this.fruitsCutInRow = new FruitsCutInRow(0);
    this.comboCount = 0;
    this.frenzyMode = false;
    this.gameMode = gameMode;
    this.currentLevel = new Level(1);
  }

  /**
   * Private default constructor so that a game may not be initialized with the default java constructor
   */
  @SuppressWarnings("unused")
  private GameModel() {

  }

  /**
   * Updates the level by adding one to the level class
   */
  public void updateLevel() {
    this.currentLevel.increaseLevel();
  }

  /**
   * Gets the level we are currently on from the level class
   * 
   * @return Our current level in int form
   */
  public int getLevel() {
    return this.currentLevel.getLevel();
  }

  /**
   * Gets whether or not we are in frenzy mode from the score class
   * 
   * @return True iff we are in frenzy mode, false otherwise
   */
  public boolean getFrenzyMode() {
    return this.frenzyMode;
  }

  /**
   * Sets whether or not we currently want to be in frenzy mode
   * 
   * @param set True iff we are entering frenzy mode
   */
  public void setFrenzyMode(boolean set) {
    this.frenzyMode = set;
  }

  /**
   * Sets whether or not we are in double score mode
   * 
   * @param set True iff we are in double score mode, false otherwise
   */
  public void setDoubleScore(boolean set) {
    this.score.setDouble(set);
  }

  /**
   * Updates the score by adding a given number of points to it
   * 
   * @param points The amount of points to add to the score
   */
  public void updateScore(int points) {
    this.score.setScore(points);
  }

  /**
   * Gets the score value in integer form
   * 
   * @return the current score
   */
  public int getScore() {
    return this.score.getScore();
  }

  /**
   * Gets the current number of hearts
   * 
   * @return The current number of hearts
   */
  public int getHearts() {
    return this.hearts.getHearts();
  }

  /**
   * Adds another life to the model unless we have reached the maximum number allowed
   * 
   * @return True iff we are able to add another heart (hearts < 3). False otherwise
   */
  public boolean addHeart() {
    return this.hearts.addHearts();

  }

  /**
   * Subtracts one life from the model unless we have reached the minimum number allowed
   * 
   * @return True iff we are able to remove another heart (hearts > 0). False otherwise
   */
  public boolean removeHeart() {
    return this.hearts.removeHearts();
  }

  /**
   * Updates the total number of fruits cut by one.
   */
  public void updateNumberOfFruitsCut() {
    this.numberOfFruitsCut.updateFruitsCut();
  }

  public int getNumberOfFruitsCut() {
    return this.numberOfFruitsCut.getFruitsCut();
  }

  /**
   * Updates the total number of fruits cut in a row by one.
   */
  public void updateNumberOfFruitsCutInRow() {
    this.fruitsCutInRow.updateFruitsCutInRow();
  }

  /**
   * Resets the number of fruits cut in a row.
   */
  public void resetNumberOfFruitsCutInRow() {
    this.fruitsCutInRow.resetFruitsCutInRow();
  }

  public int getNumberOfFruitsCutInRow() {
    return this.fruitsCutInRow.getMax();
  }

  /**
   * Updates the number of fruits cut in one fell slice by one. Used by the Cutter when it determines multiple fruit have been cut.
   */
  public void updateComboCount() {
    this.comboCount++;
  }

  /**
   * Resets the number of fruits cut in one fell slice. Used by the Cutter when it determines the cut has ended.
   */
  public void resetComboCount() {
    this.comboCount = 0;
  }

  /**
   * Gets the current number of fruits that have been cut in one fell slice
   * 
   * @return The number of fruits cut in one slice
   */
  public int getComboCount() {
    return comboCount;
  }

  /**
   * Adds an observer to the observable component.
   * 
   * @param scoreObserver The observer watching the score to update them on the screen.
   */
  public void addScoreObserver(ScoreObserver scoreObserver) {
    this.score.addObserver(scoreObserver);
  }

  /**
   * Adds an observer to the observable component.
   * 
   * @param heartObserver The observer watching the hearts to draw them on the screen.
   */
  public void addHeartObserver(HeartObserver heartObserver) {
    this.hearts.addObserver(heartObserver);
  }

  /**
   * Adds an observer to the observable component.
   * 
   * @param totalFruitObserver The observer watching the total number of fruit cut to update them on the screen.
   */
  public void addTotalFruitObserver(TotalFruitObserver totalFruitObserver) {
    this.numberOfFruitsCut.addObserver(totalFruitObserver);
  }

  /**
   * Adds an observer to the observable component.
   * 
   * @param fruitCutInRowObserver The observer watching the number of fruits cut in a row to update them on the screen.
   */
  public void addFruitCutInRowObserver(FruitCutInRowObserver fruitCutInRowObserver) {
    this.fruitsCutInRow.addObserver(fruitCutInRowObserver);
  }

  public void addLevelObserver(LevelObserver levelObserver) {
    this.currentLevel.addObserver(levelObserver);
  }
}
