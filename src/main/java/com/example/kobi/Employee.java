package com.example.kobi;

public class Employee {
    protected int code;
    protected String name;
    protected int[][] constraints; //2 shifts X 7 days
    protected int[][] expandConstraints; //6 shifts X 7 days
    protected int[] ability; //[shiftHead , control, securityGuard, driver]
    protected String sentTime="0";

    public Employee(int newCode, String newName, int[] newAbility){
        code=newCode;
        name=newName;
        ability=newAbility;
        constraints=new int[2][7];
        expandConstraints=new int[6][7];
    }
    public void setSentTime(String newTime){
        sentTime=newTime;
    }
    public void setConstraints(int[][] newConstraints){
        constraints=newConstraints;
        for(int i=0; i< 2; i++)
            for(int j=0; j< 7; j++){
                expandConstraints[i*3][j]=this.ability[0]*constraints[i][j];
                expandConstraints[i*3+1][j]=this.ability[1]*constraints[i][j];
                expandConstraints[i*3+2][j]=this.ability[2]*constraints[i][j];
            }
    }


}
