# AnimalEvolutionSymulator
Symulator ewolucujny, przedstawia pewną populacje zwierząt na mapie o określonych rozmiarach.
Zasady symulacji:
- zwierząta rodzą się w wyznaczonej na mapie dżungli
- zwierząta potrzebują na przeżycie dnia pewną pulę energi
- odzyskują energię za zjedzenie roślinki
- dziennie pojawiają się dwie roślinki jedna w obrębie całej mapy, druga w obrębie wyznaczonej dżungli
- zwierząta mogą się rozmnażać gdy wejdą na jedno pole i oba osobnika mają więcej niż 50% energi
- dzieci dziedziczą genom po obojgu rodziców
- genom definiuje prawdopodobnieństwo wykonania ruchu w danym kierunku

Symulator umożliwia:
- wstrzymywanie i wznawianie działania z poziomu interfejsu
- pozwala monitorować parametry takie jak:
   * liczby wszystkich zwierząt
   * liczby wszystkich roślin
   * dominujących genotypów
   * średniego poziomu energii dla żyjących zwierząt
   * średniej długości życia zwierząt dla martwych zwierząt
   * średniej liczby dzieci dla żyjących zwierząt
- pozwala wybrac konkretnego osobnika w celu zbadania jego parametrów:
   * Id
   * Energia
   * Dzień urodzenia
   * Dzień śmierci (jeśli umrze w czasie podglądu)
   * Liczbę dzieci
   * Liczbę dzieci od momentu rozpoczecia obserwacji z rozdzieleniem na żyjące i martwe
   * Liczbę potomków od momentu rozpoczecia obserwacji z rozdzieleniem na żyjących i martwych
   * Genom konkretnego osobnika
- Pozwala na uruchomienie dwóch niezależnych od siebie symulacji (w interfejsie wyświetlane są zawsze statystyki dilnej symulacji)
- Pozwala na monipulowanie parametrami uruchominiowymi aplikacji
- Pozwala na zapis statystyk z danego dnia do pliku tekstowego
- Pozwala na podświetlenie osobników z najpopularniejszym genomem
- Po wybraniu śledzonego osobnika podświetla również jego dzieci i potomków

<h5>Link do wizualizacji z działania programu:</h5> https://youtu.be/Lw1ZQzhyKZQ

<h3>Uruchomienie projektu<br></h3>
- Do uruchomienia projektu niezbędna jest biblioteka java fx
- ponaddto w pliku GUI.fxml należy podmienić ścieżki plików jpg z "file:/D:/AnimalEvolutionSymulator/src/EvolutionSymulator/GUI/Images" na "file:[ŚCIEŻKA BEZWZGLĘDNA PROJEKTU]/AnimalEvolutionSymulator/src/EvolutionSymulator/GUI/Images"

  
