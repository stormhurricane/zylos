package com.zylos.backend.database;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;


@Entity
@Table(name = "TERMIN")
@Deprecated(since = "2024-06", forRemoval = true)
public class Termin {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @NotNull
        private int id;

        private int lvId;

        private String jahr;

        private String monat;

        private String tag;

        private String uhrzeit;

        private String betreff;

        public Termin() {
        }

        public Termin(int lvId, String jahr, String monat, String tag, String uhrzeit, String betreff) {
            this.lvId = lvId;
            this.jahr = jahr;
            this.monat = monat;
            this.tag = tag;
            this.uhrzeit = uhrzeit;
            this.betreff = betreff;
        }

        public int getId() {
            return id;
        }

        public int getlvId() {
            return lvId;
        }

        public void setlvId(int lvId) {
            this.lvId = lvId;
        }

        public String getUhrzeit() {
            return uhrzeit;
        }

        public void setUhrzeit(String uhrzeit) {
            this.uhrzeit = uhrzeit;
        }

        public String getBetreff() {
            return betreff;
        }

        public void setBetreff(String betreff) {
            this.betreff = betreff;
        }

        public String getJahr() {
            return jahr;
        }

        public String getMonat() {
            return monat;
        }

        public String getTag() {
            return tag;
        }

        public void setJahr(String jahr) {
            this.jahr = jahr;
        }

        public void setMonat(String monat) {
            this.monat = monat;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
