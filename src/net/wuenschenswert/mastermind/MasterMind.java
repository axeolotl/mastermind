package net.wuenschenswert.mastermind;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MasterMind {
  final int länge;
  final List<BewertetesMuster> versuche = new ArrayList<>();
  final Function<Muster, Bewertung> bewerter;
  final Random random = new Random();

  public MasterMind(int länge, Function<Muster, Bewertung> bewerter) {
    this.länge = länge;
    this.bewerter = bewerter;
  }

  public static void main(String[] args) {
    Function<Muster, Bewertung> musterBewertungFunction;
    int länge;
    if (Arrays.asList("-i").equals(Arrays.asList(args))) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      länge = 4; // hüstel
      musterBewertungFunction = muster -> {
        System.out.println("Versuch: "+muster);
        System.out.print("Bewertung schwarze? ");
        int schwarz = readInt(reader);
        System.out.print("Bewertung weiß? ");
        int weiß = readInt(reader);
        return new Bewertung(schwarz, weiß);
      };
    } else {
      Farbe[] farben = Arrays.stream(args).map(Farbe::valueOf).toArray(Farbe[]::new);
      länge = farben.length;
      Muster richtig = new Muster(farben);
      musterBewertungFunction = muster -> Bewertung.bewerte(muster, richtig);
    }
    MasterMind masterMind = new MasterMind(länge, musterBewertungFunction);
    masterMind.play();
  }

  private static int readInt(BufferedReader reader) {
    try {
      return Integer.parseInt(reader.readLine());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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
    Map<Double, List<Muster>> musterByEntropie = möglicheLösungen.stream()
        .collect(Collectors.groupingBy(möglicherNächsterVersuch -> entropy(möglicherNächsterVersuch, möglicheLösungen)));
    if (möglicheLösungen.size() < 50) {
      System.out.println("  " + musterByEntropie);
    }
    Optional<Double> maxEntropieOpt = musterByEntropie.keySet().stream().max(Comparator.comparingDouble(d -> d));
    if(!maxEntropieOpt.isPresent()) {
      return Optional.empty();
    }
    List<Muster> nächsteVersucheMitHöchsterEntropie =
        musterByEntropie.get(maxEntropieOpt.get());
    Muster nächsterVersuchMitHöchsterEntropie =
        nächsteVersucheMitHöchsterEntropie.get(random.nextInt(nächsteVersucheMitHöchsterEntropie.size()));
    return Optional.of(nächsterVersuchMitHöchsterEntropie);
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
}
