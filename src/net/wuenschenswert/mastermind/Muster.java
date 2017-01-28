package net.wuenschenswert.mastermind;

import java.util.*;
import java.util.stream.Stream;

public class Muster {
  Farbe f[];

  public Muster(Farbe... f) {
    this.f = f;
  }

  public int length() {
    return f.length;
  }

  public Farbe get(int i) {
    return f[i];
  }

  public Muster extendedWith(Farbe farbe) {
    Farbe[] fx = new Farbe[length()+1];
    System.arraycopy(f, 0, fx, 0, f.length);
    fx[f.length] = farbe;
    return new Muster(fx);
  }

  private static final int ANZ_FARBEN = Farbe.values().length;

  int[] getHistogram() {
    int[] result = new int[ANZ_FARBEN];
    for(int i=0; i<f.length; ++i) {
      result[get(i).ordinal()]++;
    }
    return result;
  }

  static Stream<Muster> alleMuster(int länge) {
    List<Farbe> farben = Arrays.asList(Farbe.values());
    Stream<Muster> stream = Stream.of(new Muster());
    for(int i=0; i<länge; ++i) {
      stream = stream.flatMap(muster -> farben.stream().map(muster::extendedWith));
    }
    return stream;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Muster muster = (Muster) o;

    // Probably incorrect - comparing Object[] arrays with Arrays.equals
    return Arrays.equals(f, muster.f);

  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(f);
  }

  @Override
  public String toString() {
    return "Muster{" + Arrays.toString(f) + '}';
  }
}
