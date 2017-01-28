package net.wuenschenswert.mastermind;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MasterMind {
  final int länge;
  final List<BewertetesMuster> versuche = new ArrayList<>();
  final Function<Muster, Bewertung> bewerter;

  public MasterMind(int länge, Function<Muster, Bewertung> bewerter) {
    this.länge = länge;
    this.bewerter = bewerter;
  }

  public static void main(String[] args) {
    Farbe[] farben = Arrays.stream(args).map(Farbe::valueOf).toArray(Farbe[]::new);
    Muster richtig = new Muster(farben);
    MasterMind masterMind = new MasterMind(farben.length, muster -> Bewertung.bewerte(muster, richtig));
    masterMind.play();
  }

  private void play() {
    while (step())
      ;
  }

  private boolean step() {
    Optional<Muster> musterOpt = besterNächsterVersuch();
    if(!musterOpt.isPresent()) {
      // Widerspruch ?!
      return false;
    }
    Muster versuch = musterOpt.get();
    System.out.println("Versuch #"+(versuche.size()+1)+": "+versuch);
    Bewertung bewertung = bewerter.apply(versuch);
    System.out.println("Bewertung: "+bewertung);
    versuche.add(new BewertetesMuster(versuch, bewertung));
    if (bewertung.schwarz == länge) {
      // Treffer
      return false;
    }
    // weiter raten...
    return true;
  }


  Stream<Muster> möglicheLösungen() {
    return
        Muster.alleMuster(länge)
            .filter(muster -> versuche.stream().allMatch(versuch -> versuch.istKonsistentMit(muster)));
  }

  Optional<Muster> besterNächsterVersuch() {
    List<Muster> möglicheLösungen = Arrays.asList(möglicheLösungen().toArray(Muster[]::new));
    System.out.println("Anzahl möglicher Lösungen: "+möglicheLösungen.size());
    boolean debug = möglicheLösungen.size() < 50;
    Optional<MusterMitEntropie> nächsterVersuchMitHöchsterEntropie = möglicheLösungen.stream()
        .map(möglicherNächsterVersuch ->
            new MusterMitEntropie(möglicherNächsterVersuch, entropy(möglicherNächsterVersuch, möglicheLösungen)))
        .map(mme -> {
          if (debug) { System.out.println("  " + mme); }
          return mme;
        })
        .collect(Collectors.maxBy(Comparator.comparingDouble(MusterMitEntropie::getEntropie)));
    return nächsterVersuchMitHöchsterEntropie.map(MusterMitEntropie::getMuster);
  }

  double entropy(Muster muster, List<Muster> möglicheLösungen) {
    Map<Bewertung, Long> histogramm = möglicheLösungen.stream().collect(
        Collectors.groupingBy(angenommeneLösung -> Bewertung.bewerte(muster, angenommeneLösung),
            Collectors.counting()));
    Collection<Long> verteilung = histogramm.values();
    long total = verteilung.stream().collect(Collectors.summingLong(Long::longValue));
    return verteilung.stream().map(l -> {
      double p = (double) l / total;
      return -p * Math.log(p);
    }).collect(Collectors.summingDouble(Double::doubleValue));
  }

  static class MusterMitEntropie {
    private final Muster muster;
    private final double entropie;

    public MusterMitEntropie(Muster muster, double entropie) {
      this.muster = muster;
      this.entropie = entropie;
    }

    public Muster getMuster() {
      return muster;
    }

    public double getEntropie() {
      return entropie;
    }

    @Override
    public String toString() {
      return "MusterMitEntropie{" +
          "muster=" + muster +
          ", entropie=" + entropie +
          '}';
    }
  }
}
