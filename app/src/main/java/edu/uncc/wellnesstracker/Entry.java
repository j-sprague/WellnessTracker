package edu.uncc.wellnesstracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Entity(tableName = "entry")
public class Entry {

    @PrimaryKey(autoGenerate = true)
    public int id;

    private Date time;

    public int quality;
    public double sleep, exercise, weight;

    public Entry() {}

    public Entry( int quality, double sleep, double exercise, double weight, Date time) {
        this.quality = quality;
        this.sleep = sleep;
        this.exercise = exercise;
        this.weight = weight;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSleep() {
        return sleep;
    }

    public void setSleep(double sleep) {
        this.sleep = sleep;
    }

    public double getExercise() {
        return exercise;
    }

    public void setExercise(double exercise) {
        this.exercise = exercise;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getTime() {
        return time;
    }


    public void setTime(Date time) {
        this.time = time;
    }
}
