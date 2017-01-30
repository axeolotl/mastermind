package net.wuenschenswert.mastermind;

public class Bewertung {
  int schwarz, weiß;

  public Bewertung(int schwarz, int weiß) {
    assert schwarz >= 0;
    assert weiß >= 0;
    // assert schwarz + weiß <= 4;
    // some combinations like 3/1 still don't make sense
    this.schwarz = schwarz;
    this.weiß = weiß;
  }

  private static final int ANZ_FARBEN = Farbe.values().length;

  public static Bewertung bewerte(Muster geraten, Muster richtig) {
    int richtigePosition = 0;
    assert geraten.length() == richtig.length();
    for (int i=0; i<geraten.length(); ++i) {
      if (geraten.get(i).equals(richtig.get(i))) {
        ++richtigePosition;
      }
    }
    int[] histogrammGeraten = geraten.getHistogram();
    int[] histogrammRichtig = richtig.getHistogram();
    int farbÜbereinstimmung = 0;
    for(int i=0; i<ANZ_FARBEN; ++i) {
      farbÜbereinstimmung += Math.min(histogrammGeraten[i],histogrammRichtig[i]);
    }
    int richtigeFarbeFalschePosition = farbÜbereinstimmung - richtigePosition;

    return new Bewertung(richtigePosition, richtigeFarbeFalschePosition);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Bewertung bewertung = (Bewertung) o;

    if (schwarz != bewertung.schwarz) return false;
    return weiß == bewertung.weiß;

  }

  @Override
  public int hashCode() {
    int result = schwarz;
    result = 31 * result + weiß;
    return result;
  }

  @Override
  public String toString() {
    return "Bewertung{" +
        "schwarz=" + schwarz +
        ", weiß=" + weiß +
        '}';
  }
}
