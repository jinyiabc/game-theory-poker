import _misc.Constants;
import _misc.Helper;
import stage3.*;
import java.io.*;
import java.util.*;
import stage3.InfoSet.*;

import _io.*;

public class MyTest {

  public static double[][] startPDT;
  public static float[][][][] transition0to3;
  public static float[][][][] transition3to4;
  public static float[][][][] transition4to5;
  public static float[][][][][] transition;
  public static double[][][] terminalValues;
  public static int[] numClusters;

  private static int maxNumBoardCards;

  // LP constants
  private final static String rowObjective = " N";
  private final static String rowEqual = " E";
  private final static String rowGreater = " G";
  private final static String rewardConstraintPrefix = "r";
  private final static String planConstraintPrefix = "p";
  private final static String xPrefix = "x";
  private final static String yPrefix = "y";
  private final static String zPrefix = "z";
  private final static String rhsColumnName = "mRHS";
  public static boolean writeToDisk = true;


  // file access constants

  public static final String ROOT_INPUT_DIR = Constants.DATA_FILE_REPOSITORY +
      "stage2" + Constants.dirSep;

  public static final String ROOT_GAME_INPUT_DIR = Constants.DATA_FILE_REPOSITORY +
      "stage3" + Constants.dirSep;

  public static final String ROOT_OUTPUT_DIR = Constants.DATA_FILE_REPOSITORY +
      "stage3" + Constants.dirSep;

  private static final int MAX_SIMULT_FILES_OPEN = 1;
  public final static float bigBlind = 1;
  public final static float smallBlind = (float)0.5;
  public static long numLeafNodes = -1;
	public static long fiveThousandths = -1;

  public static void main(String[] args) throws Exception {
    int n;
    int m;

    // // load up constraint matrices
    // String inFile = "/Users/jinyi/Desktop/game-theory-poker/poker_data/14-card holdem/stage3/b/" + "constraints.p1.obj";
    // Object[] cmInfo = ReadBinaryConstraintMatrix.getCm(inFile);
    // maxNumBoardCards = ((Integer) cmInfo[1]).intValue();
    // n = ((Integer) cmInfo[2]).intValue();
    // ConstraintMatrix cmP1 = (ConstraintMatrix) cmInfo[0];

    // inFile = "/Users/jinyi/Desktop/game-theory-poker/poker_data/14-card holdem/stage3/b/" + "constraints.p2.obj";
    // cmInfo = ReadBinaryConstraintMatrix.getCm(inFile);
    // m = ((Integer) cmInfo[2]).intValue();
    // ConstraintMatrix cmP2 = (ConstraintMatrix) cmInfo[0];


    try {
      // // load up constraint matrices
      // String inFile = "/Users/jinyi/Desktop/game-theory-poker/poker_data/14-card holdem/stage3/b/" + "constraints.p1.obj";
      // Object[] cmInfo = ReadBinaryConstraintMatrix.getCm(inFile);
      // maxNumBoardCards = ((Integer) cmInfo[1]).intValue();
      // n = ((Integer) cmInfo[2]).intValue();
      // ConstraintMatrix cmP1 = (ConstraintMatrix) cmInfo[0];
      //
      // inFile = "/Users/jinyi/Desktop/game-theory-poker/poker_data/14-card holdem/stage3/b/" + "constraints.p2.obj";
      // cmInfo = ReadBinaryConstraintMatrix.getCm(inFile);
      // m = ((Integer) cmInfo[2]).intValue();
      // ConstraintMatrix cmP2 = (ConstraintMatrix) cmInfo[0];

      // String inFile = "/Users/jinyi/Desktop/game-theory-poker/poker_data/14-card holdem/stage3/b/" + "rewardMatrix.preprocessed.p1";
      // ReadBinaryRmStream rmStreamP1 = new ReadBinaryRmStream(inFile,
      //     Helper.getBufferSize(MAX_SIMULT_FILES_OPEN), false);
      // // String outFile = "/Users/jinyi/Desktop/game-theory-poker/poker_data/14-card holdem/stage3/b/test.mps";
    	// // writeLP(rmStreamP1, new ConstraintMatrix[] {cmP1, cmP2}, n, m,
    	// // 		outFile, xPrefix);
      //
      // RewardMatrixElement rme = rmStreamP1.getRme();
      // System.out.println(rme);
      // rmStreamP1.close();

      String inDir = "/Users/jinyi/Desktop/game-theory-poker/poker_data/14-card holdem/stage3/b/";
      String outDir = "/Users/jinyi/Desktop/game-theory-poker/poker_data/14-card holdem/stage3/b/";

      // populateMetaData(startInFile);
      // System.out.println(numClusters);
      // System.out.println(maxNumBoardCards);

      // String inFile = "/Users/jinyi/Desktop/game-theory-poker/poker_data/14-card holdem/stage3/b/" + "rewardMatrix.preprocessed.p1";
      // ReadBinaryRmStream rmStreamP1 = new ReadBinaryRmStream(inFile,
      //     Helper.getBufferSize(MAX_SIMULT_FILES_OPEN), false);
      // RewardMatrixElement rme = rmStreamP1.getRme();
      // System.out.println(rme);
      // rmStreamP1.close();

      Object[] inData = LoadInputData.getTreeData(true, ROOT_INPUT_DIR);
      startPDT = (double[][]) inData[0];
      transition0to3 = (float[][][][]) inData[1];
      transition3to4 = (float[][][][]) inData[2];
      transition4to5 = (float[][][][]) inData[3];
      terminalValues = (double[][][]) inData[4];

      numClusters = new int[] {
          startPDT.length,				// 0 bc's
          -1,								// 1
          -1,								// 2
          transition3to4.length,			// 3 bc's
          transition4to5.length,			// 4 bc's
          terminalValues[0].length			// 5 bc's
      };

      int startNumBoardCards = 3;
      int endNumBoardCards = 5;
      float initialPotP1 = smallBlind;
      float initialPotP2 = bigBlind;

      populateNumLeafNodes(endNumBoardCards, false);
      GameState.initStaticState(startNumBoardCards, endNumBoardCards,
          initialPotP1, initialPotP2);
      GameState.ensureInitRoots();
      if(writeToDisk) {
        String rmeFileName = outDir + "rewardMatrix";
        Helper.prepFilePath(rmeFileName);
        GameState.rewardMatrixOut = new WriteBinaryRmeStream(rmeFileName,
            Helper.getBufferSize(MAX_SIMULT_FILES_OPEN), numClusters, endNumBoardCards);
      }
      GameState subtreeRoot = new GameState((byte)startNumBoardCards,
          (byte)-1, (byte)-1, true, Float.NaN,
          false, (byte)-1, (byte)-1, Float.NaN, Float.NaN, null);
      subtreeRoot.expand();

      if(writeToDisk) {
        GameState.rewardMatrixOut.close();
      }
      // String startInFile = inDir + "rewardMatrix";
      // String rewardMatrixTransposed = outDir + "rewardMatrix.transposed";
      // transposeMatrix(startInFile, rewardMatrixTransposed,
      //     Helper.getBufferSize(MAX_SIMULT_FILES_OPEN));
      // RewardMatrixElement.compareOnFirstDim = false;
      // sortRewardMatrix(rewardMatrixTransposed,
      //     Helper.getBufferSize(MAX_SIMULT_FILES_OPEN));
      // new File(rewardMatrixTransposed).delete();
      // String outName = outDir + "rewardMatrix.preprocessed.p1";
      // pruneRewardMatrix(outName, Helper.getBufferSize(MAX_SIMULT_FILES_OPEN));

    }
    catch(IOException e) {
      e.printStackTrace();
    }
    // prep reward matrix streams
    // String inFile = "/Users/jinyi/Desktop/game-theory-poker/poker_data/14-card holdem/stage3/b/" + "rewardMatrix.preprocessed.p1";
    // ReadBinaryRmStream rmStreamP1 = new ReadBinaryRmStream(inFile,
    //     Helper.getBufferSize(MAX_SIMULT_FILES_OPEN), false);
    //
    // // String outFile = "/Users/jinyi/Desktop/game-theory-poker/poker_data/14-card holdem/stage3/b/game.p1.mps";
  	// // writeLP(rmStreamP1, new ConstraintMatrix[] {cmP1, cmP2}, n, m,
  	// // 		outFile, xPrefix);
  	// rmStreamP1.close();

  }
  private static String currentWorkingFileName;

	private static void sortRewardMatrix(String inFile, int bufSize) throws IOException {
		ReadBinaryRmStream in = new ReadBinaryRmStream(inFile, bufSize, false);
		numClusters = in.numClusters;
		maxNumBoardCards = in.maxNumBoardCards;

		int recordCount = (int) (new File(inFile).length() - Constants.HEADER_SIZE ) /
				RewardMatrixElement.RECORD_SIZE;

		RewardMatrixElement[] toBeSorted = new RewardMatrixElement[recordCount];
		for(int i = 0; i < recordCount; i++) {
			toBeSorted[i] = in.getRme();

			if(toBeSorted[i] == null) {
				// recordCount is too high
				throw new RuntimeException();
			}
		}
		if(in.getRme() != null) {
			// recordCount is too low
			throw new RuntimeException();
		}
		in.close();

		Arrays.sort(toBeSorted);

		String outFileName = inFile + ".sorted";
		WriteBinaryRmeStream out = new WriteBinaryRmeStream(outFileName,
				bufSize, numClusters, maxNumBoardCards);
		for(int i = 0; i < recordCount; i++) {
			out.writeRme(toBeSorted[i]);
		}
		out.close();

		currentWorkingFileName = outFileName;
	}


	private static void pruneRewardMatrix(String outName, int bufSize) throws IOException {
		// remove duplicates
		//   (we're not worried about removing records with value==0 since that is now
		//    done in the previous step)
    currentWorkingFileName = "/Users/jinyi/Desktop/game-theory-poker/poker_data/14-card holdem/stage3/b/rewardMatrix";
		ReadBinaryRmStream in = new ReadBinaryRmStream(currentWorkingFileName, bufSize, true);

		WriteBinaryRmeStream out =
			new WriteBinaryRmeStream(outName, bufSize, numClusters, maxNumBoardCards);

		in.getRmeViaContainer();
		int firstDim = -1;
		int secondDim = -1;
		float val;
		while(in.container != null) {
			if(in.container.firstDim == firstDim) {
				if(in.container.secondDim == secondDim) {
					in.getRmeViaContainer();
					continue;
				}
			}

			firstDim = in.container.firstDim;
			secondDim = in.container.secondDim;
			val = in.container.value;
      for (int k = 0; k<100 ;k++ ) {
        System.out.println(String.format("%d %d %f", firstDim, secondDim, val));
      }
			out.writeRme(firstDim, secondDim, val);

			in.getRmeViaContainer();
		}

		out.close();
		in.close();

		new File(currentWorkingFileName).delete();
	}

  private static void populateMetaData(String inFile) throws IOException {
		ReadBinaryRmStream in = new ReadBinaryRmStream(inFile, 10000, true);
		numClusters = in.numClusters;
		maxNumBoardCards = in.maxNumBoardCards;
		in.close();
		in = null;
	}

  private static void transposeMatrix(String inFile, String outFile, int bufSize)
			throws IOException {

		populateMetaData(inFile);

		ReadBinaryRmStream in = new ReadBinaryRmStream(inFile, bufSize, true);

		File fOut = new File(outFile);
		int counter = 0;
		while(fOut.exists()) {
			fOut.delete();
			if(counter++ == 2000) {
				throw new RuntimeException();
			}
		}

		WriteBinaryRmeStream out =
			new WriteBinaryRmeStream(outFile, bufSize, numClusters, maxNumBoardCards);

		in.getRmeViaContainer();
		int firstDim = -1;
		int secondDim = -1;
		float val;
		while(in.container != null) {

			secondDim = in.container.firstDim;
			firstDim = in.container.secondDim;
			val = in.container.value;

			out.writeRme(firstDim, secondDim, val);

			in.getRmeViaContainer();
		}

		out.close();
		in.close();
	}

  private static void populateNumLeafNodes(int maxNumBoardCards, boolean isRoot) {
    // calculate numLeafNodes
    int tmpBc = maxNumBoardCards;
    numLeafNodes = InfoToken.numLeafNodes[tmpBc] +
        InfoToken.numBrContinuations[tmpBc];
    while(true) {
      numLeafNodes *= Helper.sqr(numClusters[tmpBc]);

      tmpBc = Helper.getPreviousBoardCardCount(tmpBc, true);
      if((isRoot && tmpBc == Byte.MIN_VALUE) || (!isRoot && tmpBc == 0)) {
        break;
      }

      numLeafNodes *= InfoToken.numBrContinuations[tmpBc];
      numLeafNodes += InfoToken.numLeafNodes[tmpBc];
    }
    fiveThousandths = (int) Math.floor(numLeafNodes / 200);
  }

  // private static void writeLP(ReadBinaryRmStream rmIn,
  //     ConstraintMatrix[] constraintMatrices, int numPriDim, int numSecDim,
  //     String fileName, String varSolveForPrefix) throws IOException {
  //
  //   // numRows and numCol refers to rewardMatrix
  //   System.out.println("  numRows = " + numPriDim);
  //   System.out.println("  numCols = " + numSecDim);
  //
  //
  //   ConstraintMatrix constraintPri = constraintMatrices[0];
  //   ConstraintMatrix constraintSec = constraintMatrices[1];
  //   int[] numConstraints = new int[] { constraintPri.getNumConstraints(),
  //       constraintSec.getNumConstraints() };
  //
  //   Helper.prepFilePath(fileName);
  //   WriteMPS out = new WriteMPS(fileName, Helper.getBufferSize(MAX_SIMULT_FILES_OPEN));
  //
  //   out.print(new String[] {"NAME", null, "POKER"});
  //
  //   // ROWS section
  //   out.print("ROWS");
  //   out.print(new String[] {rowObjective, "OBJ"});
  //   for(int i = 0; i < numSecDim; i++) {
  //     String intName = Integer.toString(i);
  //     String rewardConstraintName = rewardConstraintPrefix + intName;
  //     String[] toWrite = new String[] {rowGreater, rewardConstraintName};
  //     out.print(toWrite);
  //   }
  //   for(int i = 0; i < numConstraints[0]; i++) {
  //     out.print(new String[] {rowEqual,
  //         planConstraintPrefix.concat( Integer.valueOf(i).toString())});
  //   }
  //
  //
  // //   // COLUMNS section
  // //   out.print("COLUMNS");
  // //   // one set of entries for each (x|y)[0..numPriDim-1]
  // //   int rewardMatrixPointer = 0;
  // //   RewardMatrixElement element;
  // //   RewardMatrixElement rme = rmIn.getRme();
  // //   System.out.println(rme);
  //
  //   // for(int i = 0; i < numPriDim; i++) {
  //   //   Integer iInt = Integer.valueOf(i);
  //   //   String columnName = null;
  //   //   //   rme is null if we're at the end of the file.  This condition can hold
  //   //   // even when there are columns left if the touple had value zero and was
  //   //   // pruned from the RM file.
  //   //   if(rme != null) {
  //   //     int iColumnName = rme.secondDim;
  //   //     columnName = varSolveForPrefix.concat(Integer.toString(iColumnName));
  //   //   }
  //   //
  //   //   // retrieve A[i][*] (row vector)
  //   //   // (rewardMatrix is a flattened representation of A[][], sorted on row index)
  //   //   while((rme != null) && (rme.secondDim==i)) {
  //   //     System.out.println(rme);
  //   //     // out.addElement(columnName,
  //   //     //     rewardConstraintPrefix.concat(Integer.toString(rme.firstDim)), rme.value);
  //   //
  //   //     // rme = rmIn.getRme();
  //   //   }
  //   //
  //   //   // retrieve E_x[*][i] (column vector)
  //   //   ConstraintMatrixColumn col = constraintPri.getColumn(iInt);
  //   //   columnName = varSolveForPrefix.concat(iInt.toString());
  //   //   for(int j = 0; j < col.numRowIdsParentOf; j++) {
  //   //     int rowId = col.rowIdsParentOf[j];
  //   //     out.addElement(columnName,
  //   //         planConstraintPrefix.concat(Integer.toString(rowId)), -1);
  //   //   }
  //   //   if(col.rowIdChildOf != -1) {
  //   //     out.addElement(columnName,
  //   //         planConstraintPrefix.concat(Integer.toString(col.rowIdChildOf)), 1);
  //   //   }
  //   // }
  //
  //   // // one set of rows for each z[0..numConstraints[1]-1]
  //   // int rootZIndex = constraintSec.getRootRowIndex();
  //   // for(int i = 0; i < numConstraints[1]; i++) {
  //   //   // retrieve E_y[i][*] (row vector)
  //   //   String columnName = zPrefix.concat( Integer.valueOf(i).toString());
  //   //
  //   //   ConstraintMatrixRow row = constraintSec.getRow(i);
  //   //
  //   //   if(row.parentName != -1) {
  //   //     // this isn't the row making empty sequence == 1
  //   //     out.addElement(columnName,
  //   //         rewardConstraintPrefix.concat(Integer.toString(row.parentName)), -1);
  //   //   }
  //   //   for(int j = 0; j < row.numChildren; j++) {
  //   //     out.addElement(columnName,
  //   //         rewardConstraintPrefix.concat(Integer.toString(row.childNames[j])), 1);
  //   //   }
  //   //
  //   //   if(i == rootZIndex) {
  //   //     out.addElement(columnName, "OBJ", 1);
  //   //   }
  //   // }
  //   //
  //   // out.flushAddElement();
  //   //
  //   //
  //   // // add RHS
  //   // out.print("RHS");
  //   // out.addElement(rhsColumnName, planConstraintPrefix.concat(
  //   //      Integer.valueOf(constraintPri.getRootRowIndex()).toString()), 1);
  //   // out.flushAddElement();
  //   //
  //   //
  //   // // add BOUNDS
  //   // out.print("BOUNDS");
  //   // for(int i = 0; i < numConstraints[1]; i++) {
  //   //   String columnName = zPrefix.concat( Integer.valueOf(i).toString());
  //   //
  //   //   out.print(new String[] {" FR", "BND1", columnName});
  //   // }
  //   //
  //   //
  //   // // end
  //   // out.print("ENDATA");
  //   //
  //   // out.close();
  //
  // }



}
