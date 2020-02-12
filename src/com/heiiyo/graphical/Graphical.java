package com.heiiyo.graphical;

import java.util.*;

public class Graphical {

    Map<City, List<City>> adj;
   List<Edge> edges;

    private Graphical(){
        this.adj = new HashMap<>();
        this.edges = new ArrayList<Edge>();
    }

    public static Graphical buildGraphical(){
        Graphical graphical = new Graphical();
        //初始化5个节点
        City cityA = new City("A");
        City cityB = new City("B");
        City cityC = new City("C");
        City cityD = new City("D");
        City cityE = new City("E");

        //邻接链表
        List<City> adjA = new LinkedList<City>();
        adjA.add(cityB);
        adjA.add(cityD);
        adjA.add(cityE);
        List<City> adjB = new LinkedList<City>();
        adjB.add(cityC);
        List<City> adjC = new LinkedList<City>();
        adjC.add(cityD);
        adjC.add(cityE);
        List<City> adjD = new LinkedList<City>();
        adjD.add(cityC);
        adjD.add(cityE);
        List<City> adjE = new LinkedList<City>();
        adjE.add(cityB);
        graphical.adj.put(cityA, adjA);
        graphical.adj.put(cityB, adjB);
        graphical.adj.put(cityC, adjC);
        graphical.adj.put(cityD, adjD);
        graphical.adj.put(cityE, adjE);

        Edge edgeAB = new Edge(cityA,cityB,5);
        Edge edgeBC = new Edge(cityB,cityC,4);
        Edge edgeCD = new Edge(cityC,cityD,8);
        Edge edgeDC = new Edge(cityD,cityC,8);
        Edge edgeDE = new Edge(cityD,cityE,6);
        Edge edgeAD = new Edge(cityA,cityD,5);
        Edge edgeCE = new Edge(cityC,cityE,2);
        Edge edgeEB = new Edge(cityE,cityB,3);
        Edge edgeAE = new Edge(cityA,cityE,7);
        graphical.edges.add(edgeAB);
        graphical.edges.add(edgeBC);
        graphical.edges.add(edgeCD);
        graphical.edges.add(edgeDC);
        graphical.edges.add(edgeDE);
        graphical.edges.add(edgeAD);
        graphical.edges.add(edgeCE);
        graphical.edges.add(edgeEB);
        graphical.edges.add(edgeAE);
        return graphical;
    }

    /**
     * 获取边
     * @param originCity
     * @param endCity
     * @return
     */
    private Edge getEdge(City originCity, City endCity){
        for(Edge edge : edges){
            if(edge.originCity.equals(originCity) && edge.endCity.equals(endCity)){
                return edge;
            }
        }
        return null;
    }

    /**
     * 求路径的距离
     * @param citys 路径数字：如{cityA,cityB,cityD}
     * @return 路径距离
     */
    private int distanceRoute(City ... citys){
        int distance = 0;
        for (int i = 0; i < citys.length-1; i++) {
            Edge edge = this.getEdge(citys[i], citys[i+1]);
            if(edge == null)
                return 0;
            distance += edge.weight;
        }
        return distance;
    }

    /**
     * 递归路径的stop数量
     * @param patentVector 递归中的父
     * @param cityEnd      目的地city
     * @param num          满足stop的数量
     * @param paths        满足条件的路径（递归传递，不变量）
     * @param type         条件满足类型（0，1）
     */
    private void dfsVisit(Vector patentVector, City cityEnd, int num, List<Vector> paths, int type){
        List<City> nextCitys = this.adj.get(patentVector.city);
        if(nextCitys == null || nextCitys.size() < 1)
            return;
        for(City city : nextCitys){
            Vector vector = new Vector(city, patentVector.weight+1);
            vector.setPreVectors(patentVector);
            if(vector.passMuster(cityEnd, num, type)){
                paths.add(vector);
            }
            if(vector.getWeight() >= num)
                break;
            dfsVisit(vector, cityEnd, num, paths, type);
        }
    }



    /**
     * 递归获取最短路径
     * @param patentVector
     * @param cityEnd
     * @param shortest
     */
    private void shortestRouteRecursion(Vector patentVector, City cityEnd, List<Vector> shortest){
        List<City> nextCitys = this.adj.get(patentVector.city);
        if(nextCitys == null || nextCitys.size() < 1)
            return;
        int num = Integer.MAX_VALUE;
        for(City city : nextCitys){
            Vector vector = new Vector(city);
            vector.setPreVectors(patentVector);
            vector.termWeight(this);
            if(shortest.size() > 0){
                num = shortest.get(0).weight;
            }
            if(vector.passMuster(cityEnd, num, 2)){
                shortest.clear();
                shortest.add(vector);
            }
            if(vector.passMuster(cityEnd, num, 0)){
                shortest.add(vector);
            }
            if(vector.getWeight() >= num || vector.preVectors.link.contains(city))
                break;
            shortestRouteRecursion(vector, cityEnd, shortest);
        }

    }

    /**
     * 递归获取最大距离路径不超过num值
     * @param patentVector
     * @param cityEnd
     * @param num
     * @param shortest
     * @param type
     */
    private void distanceRouteMaxRecursion(Vector patentVector, City cityEnd, int num, List<Vector> shortest, int type){
        List<City> nextCitys = this.adj.get(patentVector.city);
        if(nextCitys == null || nextCitys.size() < 1)
            return;
        for(City city : nextCitys){
            Vector vector = new Vector(city);
            vector.setPreVectors(patentVector);
            vector.termWeight(this);
            if(vector.passMuster(cityEnd, num, type)){
                shortest.add(vector);
            }
            if(vector.getWeight() >= num)
                break;
            distanceRouteMaxRecursion(vector, cityEnd, num,  shortest, type);
        }

    }

    private int maxRouteStop(City cityStart, City cityEnd, int num, int type){
        Vector topVector = new Vector(cityStart);
        List<Vector> paths = new ArrayList<Vector>();
        dfsVisit(topVector, cityEnd, num, paths, type);
        return paths.size();
    }

    private int distanceRouteMax(City cityStart, City cityEnd, int num, int type){
        Vector topVector = new Vector(cityStart);
        List<Vector> paths = new ArrayList<Vector>();
        this.distanceRouteMaxRecursion(topVector, cityEnd, num, paths, type);
        return paths.size();
    }

    private int shortestRoute(City cityStart, City cityEnd){
        Vector topVector = new Vector(cityStart);
        List<Vector> shortests = new ArrayList<Vector>();
        shortestRouteRecursion(topVector,cityEnd, shortests);
        if(shortests.size() == 0){
            return 0;
        }
        return shortests.get(0).weight;
    }


    /**
     * 距离结果输出
     * @param citys
     * @return
     */
    public String distanceRouteOut(City ... citys){
        int distance = this.distanceRoute(citys);
        if(this.distanceRoute(citys) == 0){
            return "NO SUCH ROUTE";
        }
        return String.valueOf(distance);
    }

    /**
     * 经过最多num站点的数量输出
     * @param cityStart
     * @param cityEnd
     * @param num
     * @param type
     * @return
     */
    public String maxRouteStopOut(City cityStart, City cityEnd, int num, int type){
        int routeNum = maxRouteStop(cityStart, cityEnd, num, type);
        if(routeNum == 0){
            return "NO SUCH ROUTE";
        }
        return String.valueOf(routeNum);
    }

    /**
     * 最短距离输出
     * @param cityStart
     * @param cityEnd
     * @return
     */
    public String shortestRouteOut(City cityStart, City cityEnd){
        int routeNum = shortestRoute(cityStart, cityEnd);
        if(routeNum == 0){
            return "NO SUCH ROUTE";
        }
        return String.valueOf(routeNum);
    }

    /**
     * 距离不超多num输出
     * @param cityStart
     * @param cityEnd
     * @param num
     * @param type
     * @return
     */
    public String distanceRouteMaxOut(City cityStart, City cityEnd, int num, int type){
        int routeNum = distanceRouteMax(cityStart, cityEnd, num, type);
        if(routeNum == 0){
            return "NO SUCH ROUTE";
        }
        return String.valueOf(routeNum);
    }


    /**
     * 存储城市对象
     */
    public static class City{

        //城市名称：A,B,C,D,C表示
        private String cityName;

        private int weight;

        public City(String cityName){
            this.cityName = cityName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            City city = (City) o;

            return cityName != null ? cityName.equals(city.cityName) : city.cityName == null;
        }

        @Override
        public int hashCode() {
            return cityName != null ? cityName.hashCode() : 0;
        }
    }

    /**
     * 城市之间的边
     * u~v u:为originCity源节点，v:为endCity相连节点
     */
    public static class Edge{
        //u
        private City originCity;
        //v
        private City endCity;
        //权重
        private int weight;

        public Edge(City originCity, City endCity, int weight){
            this.originCity = originCity;
            this.endCity = endCity;
            this.weight = weight;
        }

        public City getOriginCity() {
            return originCity;
        }

        public void setOriginCity(City originCity) {
            this.originCity = originCity;
        }

        public City getEndCity() {
            return endCity;
        }

        public void setEndCity(City endCity) {
            this.endCity = endCity;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }

    /**
     * 矢量对象，分析路径所用
     *
     */
    public class Vector {

        //城市节点
        private City city;

        //权重
        private int weight;

        //子节点
        private Vector preVectors;

        //路径a-b-c
        private List<City> link;

        public Vector(City city) {
            this(city, 0);
            this.link.add(city);
        }

        public Vector(City city, int weight) {
            this.city = city;
            this.weight = weight;
            this.link = new LinkedList<>();
        }

        public City getCity() {
            return city;
        }

        public void setCity(City city) {
            this.city = city;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public Vector getPreVectors() {
            return preVectors;
        }

        public void setPreVectors(Vector preVectors) {
            this.preVectors = preVectors;
            this.link.addAll(0, preVectors.link);
        }

        public void termWeight(Graphical graphical){
            Edge edge = graphical.getEdge(this.preVectors.city, this.city);
            this.weight = edge.weight + this.preVectors.weight;
        }

        /**
         * 判断是否符合条件
         * @param city  目标city
         * @param num   权重参考值
         * @param type  类型
         * @return
         */
        public boolean passMuster(City city, int num, int type){
            boolean muster = false;
            switch (type){
                case 0:
                    if(this.city.equals(city) && equalsWeight(num))
                        muster = true;
                    break;

                case 1:
                    if(this.city.equals(city) && lessAndEqualsWeight(num))
                        muster = true;
                    break;
                case 2:
                    if(this.city.equals(city) && lessWeight(num))
                        muster = true;
                    break;
            }
            return muster;
        }

        /*******************比较权重部分start******************/
        /**
         *
         * @param number
         * @return
         */
        public boolean equalsWeight(int number) {
            if (number == weight)
                return true;
            return false;
        }

        public boolean lessWeight (int number){
            if (weight < number)
                return true;
            return false;
        }

        public boolean lessAndEqualsWeight(int number){
            if(equalsWeight(number) || lessWeight(number))
                return true;
            return false;
        }
        /*******************比较权重部分结束******************/
        @Override
        public String toString() {
            Iterator<City> iterator = link.iterator();
            StringBuffer buffer = new StringBuffer();
            buffer.append("Vector{");
            while (iterator.hasNext()){
                City city = iterator.next();
                buffer.append(city.cityName+",");
            }
            buffer.deleteCharAt(buffer.length()-1);
            buffer.append("}");
            return buffer.toString();
        }
    }


}
