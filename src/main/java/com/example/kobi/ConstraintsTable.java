package com.example.kobi;

import java.util.ArrayList;
import java.util.Collections;

public class ConstraintsTable {
    private final int row = 6;
    private final int column=7;
    protected ArrayList<Integer>[][] table;
    protected boolean[][] hasScheduled;
    protected int[][] chooseIndexTry;

    public ConstraintsTable(){
        table= new ArrayList[row][column];
        hasScheduled= new boolean[row][column];
        chooseIndexTry=new int[row][column];

        for (int i = 0; i < row; i++)
            for (int j = 0; j < column; j++){// initializing
                table[i][j] = new ArrayList();
                hasScheduled[i][j]=false;
                chooseIndexTry[i][j]=-1;
            }
    }

    public void set(Integer employeeCode, int[][] expandConstraints){
        for(int i=0; i< row; i++)
            for(int j=0; j< column; j++)
                if (expandConstraints[i][j]!=0)
                    table[i][j].add(expandConstraints[i][j]*employeeCode);
    }
    public void shuffle(){
        // shuffle the list to make it random
        for(int i=0; i< row; i++)
            for(int j=0; j< column; j++)
                Collections.shuffle(this.table[i][j]);
    }

    public int[] findMin(){
        //find min shift
        int minSize=-1;
        outerloop:
        for(int i=0; i< row; i++)
            for(int j=0; j< column; j++)
                if(hasScheduled[i][j]==false){
                    minSize=this.table[i][j].size();
                    break outerloop;
                }


        int [] cord={-1,-1};
        for(int i=0; i< row; i++)
            for(int j=0; j< column; j++)
                if(this.table[i][j].size()<=minSize && this.hasScheduled[i][j]==false){
                    minSize=this.table[i][j].size();
                    cord[0]=i;
                    cord[1]=j;
                }

        return  cord;
    }
    public ArrayList<int[]> unableEmployee(int code, int[] coor){ //cor[shift][day]
        ArrayList<int[]> unableIndex =new ArrayList();

        for(int i=0;i<6;i++)
            if (table[i][coor[1]].contains(code)){
                table[i][coor[1]].remove(table[i][coor[1]].indexOf(code));
                int[] tempUnableIndex={i,coor[1]};
                unableIndex.add(tempUnableIndex);
            }

        if (coor[0]<3 && coor[1]>0) //day-> remove yesterday night
            for(int i=3;i<6;i++)
                if (table[i][coor[1]-1].contains(code)){
                    table[i][coor[1]-1].remove(table[i][coor[1]-1].indexOf(code));
                    int[] tempUnableIndex={i,coor[1]-1};
                    unableIndex.add(tempUnableIndex);
                }

        if (coor[0]>2 && coor[1]<6) //night-> remove tomorrow morning
            for(int i=0;i<3;i++)
                if (table[i][coor[1]+1].contains(code)){
                    table[i][coor[1]+1].remove(table[i][coor[1]+1].indexOf(code));
                    int[] tempUnableIndex={i,coor[1]+1};
                    unableIndex.add(tempUnableIndex);
                }
        return unableIndex;
    }

    public void cancelUnableEmployee(int code, ArrayList<int[]> unableIndex){ //cor[shift][day]
        for (int i=0; i<unableIndex.size(); i++)
            table[unableIndex.get(i)[0]][unableIndex.get(i)[1]].add(0,code);
    }

}
