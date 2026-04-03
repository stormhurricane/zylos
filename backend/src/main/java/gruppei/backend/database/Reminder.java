package gruppei.backend.database;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "REMINDER")
public class Reminder {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @NotNull
        private int id;

        private int terminId;

        private String jahr;

        private String monat;

        private String tag;

        private String uhrzeit;

        private FormEnum form;

        private int nutzerId;

        public Reminder() {
        }

        public Reminder(int terminId, String jahr, String monat, String tag, String uhrzeit, FormEnum form) {
            this.jahr = jahr;
            this.monat = monat;
            this.tag = tag;
            this.terminId = terminId;
            this.uhrzeit = uhrzeit;
            this.form = form;
        }

        public int getId() {
            return id;
        }

        public int getTerminId() {
            return terminId;
        }

        public void setTerminId(int terminId) {
            this.terminId = terminId;
        }

        public String getUhrzeit() {
            return uhrzeit;
        }

        public void setUhrzeit(String uhrzeit) {
            this.uhrzeit = uhrzeit;
        }

        public FormEnum getForm() {
            return form;
        }

        public void setForm(FormEnum form) {
            this.form = form;
        }

        public int getNutzerId() {
            return nutzerId;
        }

        public void setNutzerId(int nutzerId) {
            this.nutzerId = nutzerId;
        }

        public enum FormEnum { POPUP, EMAIL }

        public String getJahr() {
            return jahr;
        }

        public void setJahr(String jahr) {
            this.jahr = jahr;
        }

        public String getMonat() {
            return monat;
        }

        public void setMonat(String monat) {
            this.monat = monat;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
}
