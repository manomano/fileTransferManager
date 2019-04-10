package com.example.demo.FileInfo;



public class imageInfo implements Comparable<imageInfo>{
    private String name;
    private Long creationDate;

    public String getName(){
        return this.name;
    }

    public Long getCreationDate(){
        return this.creationDate;
    }


    public void  setName(String name){
        this.name = name;
    }

    public void setCreationDate(Long creationDate){
        this.creationDate = creationDate;

    }

    @Override
    public int compareTo(imageInfo ToCompare){

        return Math.round(this.getCreationDate() - ToCompare.getCreationDate());
    }


}




