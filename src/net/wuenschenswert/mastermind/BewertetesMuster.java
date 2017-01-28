package net.wuenschenswert.mastermind;

public class BewertetesMuster {
  Muster muster;
  Bewertung bewertung;

  public BewertetesMuster(Muster muster, Bewertung bewertung) {
    this.muster = muster;
    this.bewertung = bewertung;
  }

  boolean istKonsistentMit(Muster richtig) {
    return Bewertung.bewerte(muster, richtig).equals(bewertung);
  }
}
