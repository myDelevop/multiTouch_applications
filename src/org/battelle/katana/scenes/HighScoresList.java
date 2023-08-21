package org.battelle.katana.scenes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import org.battelle.katana.model.Mode;

public class HighScoresList
{
  private ArrayList<Integer> scoresValue;
  private ArrayList<String> scoresName;
  private String path;

  public HighScoresList(Mode gameMode)
  {
    this.scoresValue = new ArrayList();
    this.scoresName = new ArrayList();
    try
    {
      new File("resources/data").mkdir();

      switch (gameMode) {
      case ARCADE:
        this.path = "resources/data/scoresArcade.txt";
        break;
      case SUDDEN_DEATH:
        this.path = "resources/data/scoresPractice.txt";
        break;
      case PRACTICE:
        this.path = "resources/data/scoresSuddenDeath.txt";
        break;
      default:
        this.path = "resources/data/scoresArcade.txt";
      }

      BufferedReader is = new BufferedReader(
        new FileReader(this.path));
      String line = is.readLine();

      while ((line != null) && (!line.equals("\n"))) {
        int spacePos = 0;

        while (line.charAt(spacePos) != ' ') {
          spacePos++;
        }

        int tempScore = Integer.parseInt(line.substring(0, spacePos)
          .trim());

        String tempName = line.substring(spacePos).trim();
        this.scoresValue.add(Integer.valueOf(tempScore));
        this.scoresName.add(tempName);
        line = is.readLine();
      }
      is.close();
    }
    catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void addScore(int score, String name)
  {
    int index = 0;

    while ((index < this.scoresValue.size()) && (this.scoresValue.size() != 0) && 
      (score < ((Integer)this.scoresValue.get(index)).intValue())) {
      index++;
    }
    this.scoresValue.add(index, Integer.valueOf(score));
    this.scoresName.add(index, name);
    try
    {
      BufferedWriter os = new BufferedWriter(new FileWriter(this.path));
      index = 0;

      while (index < this.scoresValue.size()) {
        os.write(this.scoresValue.get(index) + " " + (String)this.scoresName.get(index));
        index++;
        os.newLine();
      }
      os.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String toString()
  {
    String returnString = "Score:\tName:\n";

    for (int i = 0; (i < 10) && (i < this.scoresValue.size()); i++) {
      returnString = returnString + this.scoresValue.get(i) + "\t" + 
        (String)this.scoresName.get(i) + "\n";
    }
    return returnString;
  }

  public String[][] getNamesAndScores()
  {
    String[][] returnStringArray = new String[2][];
    returnStringArray[0] = new String[10];
    returnStringArray[1] = new String[10];

    for (int i = 0; (i < 10) && (i < this.scoresValue.size()); i++) {
      returnStringArray[0][i] = ((Integer)this.scoresValue.get(i)).toString();
      returnStringArray[1][i] = ((String)this.scoresName.get(i));
    }
    return returnStringArray;
  }

  public int getNumScores()
  {
    return this.scoresName.size();
  }
}