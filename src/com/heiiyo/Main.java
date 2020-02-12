package com.heiiyo;

import com.heiiyo.graphical.Graphical;

public class Main {
    public static void main(String[] args){

        Graphical.City cityA = new Graphical.City("A");
        Graphical.City cityB = new Graphical.City("B");
        Graphical.City cityC = new Graphical.City("C");
        Graphical.City cityD = new Graphical.City("D");
        Graphical.City cityE = new Graphical.City("E");
        System.out.println("Output #1:"+Graphical.buildGraphical().distanceRouteOut(cityA,cityB,cityC));
        System.out.println("Output #2:"+Graphical.buildGraphical().distanceRouteOut(cityA,cityD));
        System.out.println("Output #3:"+Graphical.buildGraphical().distanceRouteOut(cityA,cityD,cityC));
        System.out.println("Output #4:"+Graphical.buildGraphical().distanceRouteOut(cityA,cityE,cityB,cityC,cityD));
        System.out.println("Output #5:"+Graphical.buildGraphical().distanceRouteOut(cityA, cityE,cityD));
        System.out.println("Output #6:"+Graphical.buildGraphical().maxRouteStopOut(cityC, cityC,3, 1));
        System.out.println("Output #7:"+Graphical.buildGraphical().maxRouteStopOut(cityA, cityC,4, 0));
        System.out.println("Output #8:"+Graphical.buildGraphical().shortestRouteOut(cityA, cityC));
        System.out.println("Output #9:"+Graphical.buildGraphical().shortestRouteOut(cityB, cityB));
        System.out.println("Output #10:"+Graphical.buildGraphical().distanceRouteMaxOut(cityC, cityC, 30, 2));

    }
}
