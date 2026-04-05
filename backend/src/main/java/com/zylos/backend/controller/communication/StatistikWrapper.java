package com.zylos.backend.controller.communication;

import java.util.Map;

public class StatistikWrapper {

    private double teilnahmequote;

    private double bestehensquote;

    private Map<Integer, Integer> versucheProTeilnehmer;

    private Map<Integer,Integer> anzahlKorrekterAntwortenEinerFrage;

    public StatistikWrapper(double teilnahmequote, double bestehensquote, Map<Integer, Integer> versucheProTeilnehmer, Map<Integer, Integer> anzahlKorrekterAntwortenEinerFrage) {
        this.teilnahmequote = teilnahmequote;
        this.bestehensquote = bestehensquote;
        this.versucheProTeilnehmer = versucheProTeilnehmer;
        this.anzahlKorrekterAntwortenEinerFrage = anzahlKorrekterAntwortenEinerFrage;
    }

    public double getTeilnahmequote() {
        return teilnahmequote;
    }

    public void setTeilnahmequote(double teilnahmequote) {
        this.teilnahmequote = teilnahmequote;
    }

    public double getBestehensquote() {
        return bestehensquote;
    }

    public void setBestehensquote(double bestehensquote) {
        this.bestehensquote = bestehensquote;
    }

    public Map<Integer, Integer> getVersucheProTeilnehmer() {
        return versucheProTeilnehmer;
    }

    public void setVersucheProTeilnehmer(Map<Integer, Integer> versucheProTeilnehmer) {
        this.versucheProTeilnehmer = versucheProTeilnehmer;
    }

    public Map<Integer, Integer> getAnzahlKorrekterAntwortenEinerFrage() {
        return anzahlKorrekterAntwortenEinerFrage;
    }

    public void setAnzahlKorrekterAntwortenEinerFrage(Map<Integer, Integer> anzahlKorrekterAntwortenEinerFrage) {
        this.anzahlKorrekterAntwortenEinerFrage = anzahlKorrekterAntwortenEinerFrage;
    }
}
