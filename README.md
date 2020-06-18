<h1 align="center"><b>Progetto ing. software anno accademico 2019/2020</b></h1>
<h2 align="center">Gruppo GC55</h2>

![alt text](https://github.com/Mark-Zampedroni/ing-sw-2020-Vanerio-Zampedroni-Zanoni/blob/master/src/main/resources/Texture2D_sorted/readmeTitle.JPG)

## Gruppo GC55
[__Stefano Vanerio__](https://github.com/Stefano-Vanerio) (890404)

[__Mark Federico Zampedroni__](https://github.com/Mark-Zampedroni) (888340)

[__Marco Zanoni__](https://github.com/Marco-Zanoni) (888608)

# Scopo
Lo scopo progettuale è l'implementazione software del gioco da tavolo Santorini utilizzando come riferimento la versione fisica prodotta dalla Roxley games. Il pattern architetturale imposto è il Model-View-Controller.

# Test e coverage (da finire)
__Qui si scrive roba sulla coverage__

# Funzionalità
Sono state implementate tutte le funzionalità richieste
- Regole Complete
- CLI
- GUI
- Socket

e due funzionalità aggiuntive (FA)

- Persistenza
- Divinità Avanzate (Advanced Gods)

# JAR
### Server
L'indirizzo IP del server dipenderà dalla connessione dove si vuole giocare:
- Se tutti i client vengono eseguiti sulla macchina in locale sarà `127.0.0.1`.
- Se in LAN si può trovare scrivendo sul terminale Linux `ifconfig` o sul terminale Windows `ipconfig`. 
- Se invece si desidera giocare con macchine non collegate alla stessa LAN si può trovare su internet il proprio [ip pubblico](https://www.whatismyip.com/it/) e [come configurare il modem correttamente](https://portforward.com/).

Per avviare il server bisogna digitare da terminale:
```
java -jar server.jar [port]
```
dove `port` è la porta desiderata per il socket, se omesso il campo viene usata quella di default. 

### Client
Il client ha implementate due interfacce grafiche distinte. Con i parametri `{cli|gui}` all'avvio è possibile scegliere se si vuole giocare su CLI o aprire la GUI.

#### CLI
Si digita da terminale:
```
java -jar client.jar cli [ip] [port]
```
dove `ip` e `port` sono l'indirizzo ip e la porta del server, se omesso/i userà `127.0.0.1` e la porta di default.

#### GUI
Per la GUI è necessario importare le dipendenze di javafx con i parametri `[--module-path]` e `[--add-module]`:

```
java --module-path "javafx-sdk-11.0.2/lib" --add-modules javafx.controls --add-modules javafx.fxml -jar client.jar gui [ip] [port]
```
dove `ip` e `port` sono equivalenti alla cli.

# Dipendenze (da finire)
I jar sono stati compilati con java X (da trovare), il programma per funzionare necessita di una versione pari o superiore a Java X. (da trovare)

L'unica dipendendenza non presente nel JDK è javaFx. E' necessario [scaricarlo separatamente](https://openjfx.io/), si può posizionare in una qualsiasi directory e correggere il path passato in `--module-path` all'esecuzione della GUI .



