package com.example.dulajdilrukshan.indoorapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class CreatePaths {

//    public LinearLayout Auditorium,Multimedia,MSCRoom,LectureHall1,Library,DCCNLab,LiftLobby,CommonRoom,WashRooms,StaffRoom;
//    public LinearLayout con1,con2,con4,main,con5,con6,con7,con8,con9,con10;

    int startlocation1index=0,destination1index=0,startlocation2index=0,destination2index=0;
    boolean isDestination1=false,isDestination2=false,isDestinationDccn=false,isStart1=false,isStart2=false;
    int index=0;
    PathActivity location=new PathActivity();


    ArrayList<String>paths=new ArrayList<>();
    ArrayList<String>path1=new ArrayList<>();
    ArrayList<String>path2=new ArrayList<>();
    ArrayList<String>path3=new ArrayList<>();
    ArrayList<String>auditoriumtomain=new ArrayList<>();
    ArrayList<String>maintostaff=new ArrayList<>();
    ArrayList<String>locationindex=new ArrayList<>();
    ArrayList<String>connectionindex=new ArrayList<>();
    ArrayList<Integer>pathsindex=new ArrayList<>();
    ArrayList<String>toDccn=new ArrayList<>();


    public void drawingpaths(Canvas canvas,float locationarray[][],float connectionarray[][])
    {

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);

        paint.setShader(new LinearGradient(0, 0, 5, 5, Color.BLUE, Color.WHITE, Shader.TileMode.REPEAT));
        paint.setStrokeWidth(5);


        //canvas.drawLine(209f,640f+20, st, yt, paint);
        //canvas.drawLine(x1, y1 - 50, x2, y2 - 50, paint);
        //canvas.drawLine(x2, y2 - 50, x3, y3 - 50, paint);


        locationindex=new ArrayList<>(Arrays.asList("Auditorium","MultimediaLab","MSCRoom","Library","LectureHallOne","DCCNLab",
                "Lift","CommonRoom","StaffRoom","WashRoom"));

        connectionindex=new ArrayList<>(Arrays.asList("con1","con2","con4","con5","con6","con7","con8","con9","con10","conMain"));

        //StartLocation
            for (int i = 0; i <paths.size()-1; i++)
            {
                for (int j = 0; j < locationindex.size(); j++)
                {
                    if (paths.get(i).equalsIgnoreCase(locationindex.get(j)))
                    {
                        for (int k = 0; k < connectionindex.size(); k++)
                        {
                            if (paths.get(i+1).equalsIgnoreCase(connectionindex.get(k)))
                            {
                                float startX = locationarray[j][0];
                                float startY = locationarray[j][1];
                                float stopX = connectionarray[k][0];
                                float stopY = connectionarray[k][1];
                                canvas.drawLine(startX, startY, stopX, stopY, paint);
                                break;
                            }
                        }

                    }


                }
            }

//
        //Connections
            for (int i = 1; i < paths.size()-1; i++)
            {
                float startX=0,startY=0,stopX=0,stopY=0;
                for (int j=0;j<connectionindex.size()-1;j++)
                {
                    if(paths.get(i).equalsIgnoreCase(connectionindex.get(j)))
                    {
                        {
                            startX = connectionarray[j][0];
                            startY = connectionarray[j][1];

//                            stopX = connectionarray[3][0];
//                            stopY = connectionarray[3][1];
//                            canvas.drawLine(startX, startY, 0, 0, paint);
                            break;
                        }

                    }

                }
                for(int j=0;j<connectionindex.size()-1;j++)
                {
                    if(paths.get(i+1).equalsIgnoreCase(connectionindex.get(j)))
                    {
                        stopX=connectionarray[j][0];
                        stopY=connectionarray[j][1];
                        canvas.drawLine(startX, startY,stopX,stopY, paint);
                        break;
                    }
                }

            }

            if(isDestinationDccn)
            {
                canvas.drawLine(connectionarray[3][0],connectionarray[3][1],connectionarray[9][0],connectionarray[9][1], paint);
            }


        //Destination
            for (int j = 0; j < locationindex.size(); j++)
            {
                if (paths.get(paths.size()-1).equalsIgnoreCase(locationindex.get(j)))
                {
                    for (int k = 0; k < connectionindex.size(); k++)
                    {
                        if (paths.get(paths.size()-2).equalsIgnoreCase(connectionindex.get(k)))
                        {
                            float startX = connectionarray[k][0];
                            float startY = connectionarray[k][1];
                            float stopX = locationarray[j][0];
                            float stopY = locationarray[j][1];
                            canvas.drawLine(startX, startY, stopX, stopY, paint);
                            break;
                        }
                    }

                }


            }
        //}


//            canvas.drawLine(connectionarray[0][0],connectionarray[0][1],connectionarray[1][0],connectionarray[1][1], paint);//Con1toCon2
//            canvas.drawLine(connectionarray[1][0],connectionarray[1][1],connectionarray[2][0],connectionarray[2][1], paint);//Con2toCon4
//            canvas.drawLine(connectionarray[2][0],connectionarray[2][1],connectionarray[3][0],connectionarray[3][1], paint);//Con4toCon5
//            canvas.drawLine(connectionarray[3][0],connectionarray[3][1],connectionarray[9][0],connectionarray[9][1], paint);//Con5toConMain
//            canvas.drawLine(connectionarray[3][0],connectionarray[3][1],connectionarray[4][0],connectionarray[4][1], paint);//Con5toCon6
//            canvas.drawLine(connectionarray[4][0],connectionarray[4][1],connectionarray[5][0],connectionarray[5][1], paint);//Con6toCon7
//            canvas.drawLine(connectionarray[5][0],connectionarray[5][1],connectionarray[7][0],connectionarray[7][1], paint);//Con7toCon9
//            canvas.drawLine(connectionarray[5][0],connectionarray[5][1],connectionarray[6][0],connectionarray[6][1], paint);//Con7toCon8
//            canvas.drawLine(connectionarray[7][0],connectionarray[7][1],connectionarray[8][0],connectionarray[8][1], paint);//Con9toCon10
//
//
//            canvas.drawLine(locationarray[0][0], locationarray[0][1],connectionarray[0][0],connectionarray[0][1], paint);//Auditorium
//            canvas.drawLine(connectionarray[0][0],connectionarray[0][1],locationarray[1][0], locationarray[1][1], paint);//Multimedia
//            canvas.drawLine(locationarray[2][0],locationarray[2][1],connectionarray[1][0],connectionarray[1][1], paint);//MSC
//            canvas.drawLine(locationarray[4][0],locationarray[4][1],connectionarray[1][0],connectionarray[1][1], paint);//Hallone
//            canvas.drawLine(connectionarray[2][0],connectionarray[2][1],locationarray[3][0],locationarray[3][1], paint);//Library
//            canvas.drawLine(connectionarray[9][0],connectionarray[9][1],locationarray[5][0],locationarray[5][1], paint);//DCCN
//            canvas.drawLine(connectionarray[3][0],connectionarray[3][1],locationarray[6][0],locationarray[6][1], paint);//Lift
//            canvas.drawLine(connectionarray[7][0],connectionarray[7][1],locationarray[7][0],locationarray[7][1], paint);//CommonRoom
//            canvas.drawLine(connectionarray[8][0],connectionarray[8][1],locationarray[9][0],locationarray[9][1], paint);//WashRoom
//            canvas.drawLine(connectionarray[6][0],connectionarray[6][1],locationarray[8][0],locationarray[8][1], paint);//StaffRoom
    }
    public void createpath(String startlocation, String destination)
    {
        auditoriumtomain=new ArrayList<>(Arrays.asList("con1","Auditorium","con1","MultimediaLab","con1","con2","MSCRoom","con2","LectureHallOne","con2"
                ,"con4","Library","con4"));

        maintostaff=new ArrayList<>(Arrays.asList("con5","Lift","con5","con6","con6","con7","con7","con9",
                "CommonRoom","con7","con9","con10","WashRoom","con7","con8","StaffRoom"));
        toDccn=new ArrayList<>(Arrays.asList("con5","conMain","DCCNLab","conMain","con5"));






        //From Auditorium to Library
            for (int j = 0; j < auditoriumtomain.size(); j++)
            {
                if (auditoriumtomain.get(j).equalsIgnoreCase(startlocation))
                    {
                        for (int i = 0; i < auditoriumtomain.size(); i++)
                        {
                            if (auditoriumtomain.get(i).equalsIgnoreCase(startlocation))
                            {
                                startlocation1index = i;
                                isStart1=true;
                                break;
                            }

                        }
                    }
            }
        for(int y=0;y<auditoriumtomain.size();y++)
        {
            if (auditoriumtomain.get(y).equalsIgnoreCase(destination))
            {

                destination1index = y;
                isDestination1=true;
                break;
            }


        }
        if (startlocation1index < destination1index&&(startlocation1index!=0&&destination1index!=0)) {
            for (int i = startlocation1index; i <= destination1index; i++) {

                {
                    path1.add(auditoriumtomain.get(i));
                }
            }
        } else
            if (startlocation1index > destination1index||(startlocation1index==0||destination1index==0))
            {
                if(!isDestination1)
                {
                    for (int i = startlocation1index; i < auditoriumtomain.size(); i++) {

                        {
                            path1.add(auditoriumtomain.get(i));
                        }
                    }
                }
                else
                {
            for (int i = destination1index; i < auditoriumtomain.size(); i++)
            {
                path1.add(auditoriumtomain.get(i));
            }
            }
        }
//Only if the Destination and Start location are in a same array
        if(isDestination1&&isStart2) {
            for (int i = 0; i < path1.size() - 1; i++) {
                if (!path1.get(i).startsWith("con")) {
                    if (!path1.get(i).equalsIgnoreCase(startlocation) || !path1.get(path1.size() - 1).equalsIgnoreCase(destination)) {
                        path1.remove(i);
                    }
                }

            }
        }
//Remove unwanted paths
        if(isDestination1||isStart2) {
            for (int i = 0; i < path1.size(); i++) {
                if (!path1.get(i).startsWith("con")) {
                    if (!path1.get(i).equalsIgnoreCase(startlocation) || !path1.get(i).equalsIgnoreCase(destination)) {
                        path1.remove(i);
                    }
                }

            }
        }




        //From con5 to StaffRoom
        for (int j = 0; j < maintostaff.size(); j++)
        {
            if (maintostaff.get(j).equalsIgnoreCase(startlocation))
            {
                for (int i = 0; i < maintostaff.size(); i++)
                {
                    if (maintostaff.get(i).equalsIgnoreCase(startlocation))
                    {
                        startlocation2index = i;
                        isStart2=true;
                        break;
                    }

                }
            }
        }
        for(int y=0;y<maintostaff.size();y++)
        {
            if (maintostaff.get(y).equalsIgnoreCase(destination))
            {

                destination2index = y;
                isDestination2=true;

                break;
            }


        }
        //toDCCN
        for(int y=0;y<toDccn.size();y++)
        {
            if (toDccn.get(y).equalsIgnoreCase(destination))
            {

                destination2index = y;
                isDestination2=true;
                isDestinationDccn=true;
                break;
            }


        }


       if(isDestinationDccn){
             if (startlocation2index < destination2index) {
                 for (int i = startlocation2index; i <= destination2index; i++) {

            {
                path3.add(toDccn.get(i));
            }
        }
             } else if (startlocation2index > destination2index) {
              if (!isDestinationDccn) {
                for (int i = startlocation2index; i < maintostaff.size(); i++) {

                {
                    path3.add(toDccn.get(i));
                }
            }
        } else {
            for (int i = startlocation2index; i >= destination2index; i--) {
                path3.add(toDccn.get(i));
            }
        }
    }}



        /////////////

        if (startlocation2index < destination2index) {
            for (int i = startlocation2index; i <= destination2index; i++) {

                {
                    path2.add(maintostaff.get(i));
                }
            }
        } else
        if (startlocation2index > destination2index)
        {
            if(!isDestination2)
            {
                for (int i = startlocation2index; i < maintostaff.size(); i++) {

                    {
                        path2.add(maintostaff.get(i));
                    }
                }
            }
            else
            {
                for (int i = startlocation2index; i >= destination2index; i--)
                {
                    path2.add(maintostaff.get(i));
                }
            }
        }
//Only if the Destination and Start location are in a same array
        if(isDestination2) {
            for (int i = 0; i < path2.size() - 1; i++) {
                if (!path2.get(i).startsWith("con")) {
                    if (!path2.get(i).equalsIgnoreCase(startlocation) || !path2.get(path2.size() - 1).equalsIgnoreCase(destination))
                    {
                        path2.remove(i);
                    }
                }

            }
        }
        if(isDestination2) {
            for (int i = 0; i < path1.size() - 1; i++) {
                if (!path1.get(i).startsWith("con")) {
                    if (!path1.get(i).equalsIgnoreCase(startlocation) ) {
                        path1.remove(i);
                    }
                }

            }
        }
//        if(isDestination2)
//        {
//            for (int i=0;i<path2.size()-1;i++)
//            {
//                if(!destination.equalsIgnoreCase("DCCNLab"))
//                {
//                    if(path2.get(i).startsWith("conMain"))
//                    {
//                        int index=path2.indexOf("conMain");
//                        path2.remove(i);
//                        break;
//                    }
//                    break;
//                }
//            }
//        }


//        //locationDCCNLAB
//        if(destination.equalsIgnoreCase("DCCNLab"))
//        {
//            for(int i=0;i<toDccn.size();i++) {
//                path3.add(toDccn.get(i));
//            }
//        }

            paths.addAll(path1);
            paths.addAll(path2);
            paths.addAll(path3);
//RemoveDuplicates

        if(!paths.isEmpty()) {
            for (int i = 1; i < paths.size() - 1; i++) {
                for (int j=1;j<paths.size()-2;j++)
                {
                    if (paths.get(i).equalsIgnoreCase(paths.get(j-1)))
                    {
                        {
                            paths.remove(j);
                            break;
                        }
                    }
                    break;
                }

            }
        }



    }

    public void findindex()
    {


    }
    public void  text(TextView textView,int index)
    {
        float locationarray[][]=new float[0][1];
        float connectionarray[][]=new float[0][1];
        createpath("Library","CommonRoom");

        {
            textView.setText(" "+paths.get(index));
            //index++;
        }


    }

}
