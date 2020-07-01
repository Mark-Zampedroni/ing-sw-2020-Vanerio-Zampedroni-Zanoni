<h1 align="center"><b>Progetto ing. software anno accademico 2019/2020</b></h1>
<h2 align="center">Gruppo GC55</h2>

![alt text](https://github.com/Mark-Zampedroni/ing-sw-2020-Vanerio-Zampedroni-Zanoni/blob/master/src/main/resources/Images/readmeTitle.JPG)

## Componenti del gruppo
[__Stefano Vanerio__](https://github.com/Stefano-Vanerio) (890404)

[__Mark Federico Zampedroni__](https://github.com/Mark-Zampedroni) (888340)

[__Marco Zanoni__](https://github.com/Marco-Zanoni) (888608)

# Scopo
Lo scopo progettuale è l'implementazione software del gioco da tavolo Santorini utilizzando come riferimento la versione fisica prodotta dalla Roxley games. Il pattern architetturale imposto è il Model-View-Controller.

# Test e coverage

I test utilizzano Junit 4.12.

Principalmente sono stati testati Model e Controller. Anche se non richiesto si è raggiunta una buona coverage per Server e messaggi.

Nella seguente tabella si riportano gli elementi con coverage più alta:

Element | Class, % | Method, % | Line, % |
--- | --- | --- | --- |
__model__ | 100% (26/26) | 100% (154/154) | 98% (474/479) |
__controller__ | 100% (9/9) | 92% (86/93) | 85% (355/413) |
__server__ | 100% (3/3) | 91% (31/34) | 81% (172/209) |
__utility__ | 91% (21/23) | 66% (52/78) | 62% (144/232) |

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
### Creazione dei JAR
E' possibile creare i JAR sia tramite Intellij sia tramite Maven `package`, dopo aver utilizzato il comando `Build Project`.
Nella cartella sono già presenti i jar finali ma, nel caso si volessero creare, è possibile farlo con la funzione `Build artifacts` o con questo procedimento utilizzando maven:
- Per il Server: selezionare il profilo `server` e deselezionare il profilo `client` all'interno del menu di Maven. 
Quindi premere il comando `package`.
- Per il Client: selezionare il profilo `client` e deselezionare il profilo `server` all'interno del menu di Maven. Modificare alla riga numero `112` del file `pom.xml` dentro il tag `<mainClass>` la classe `it.polimi.ingsw.ServerApp` con la classe `it.polimi.ingsw.ClientApp`. 
Quindi premere il comando `package`.
  
Per reimpostare la generazione del JAR per il server sostituire il contenuto della riga `112` con `it.polimi.ingsw.ServerApp` e selezionare il profilo corretto.
  
### Server
L'indirizzo IP del server dipenderà dalla connessione dove si vuole giocare:
- Se tutti i client vengono eseguiti sulla macchina in locale sarà `127.0.0.1`.
- Se in LAN si può trovare scrivendo sul terminale Linux `ifconfig` o sul terminale Windows `ipconfig`. 
- Se invece si desidera giocare con macchine non collegate alla stessa LAN si può trovare su internet il proprio [ip pubblico](https://www.whatismyip.com/it/) e [come configurare il modem correttamente](https://portforward.com/).

Per avviare il server bisogna digitare su console:
```
java -jar server.jar [-p port] [-log]
```
dove `port` è la porta desiderata per il socket, se omesso il campo viene usata quella di default. Con `log` verrà generato un file di logging nella stessa directory del jar.

### Client
Il client ha implementate due interfacce grafiche distinte. Con il parametro `{cli|gui}` all'avvio è possibile scegliere se si vuole giocare su CLI o aprire la GUI. Se omesso viene caricata la GUI.

#### CLI

In modo da poter giocare al meglio con la CLI si necessita l'utilizzo di un terminale che supporti gli ANSI escape estesi (per i colori) impostato su una codifica UTF-8. In base alla risoluzione dello schermo si potrebbe aver bisogno di diminuire la dimensione del font (per esempio: con una risoluzione di 1920x1080 l'ideale è una font-size di 14).

Per avviare la CLI dal jar si deve scrivere su console ([arg] sono argomenti opzionali):
```
java -jar client.jar cli [-i ip] [-p port] [-log]
```
dove `ip` e `port` sono  l'indirizzo ip e la porta del server, se omesso/i userà `127.0.0.1` e la porta di default. Con `log`, come per il server, verrà generato un file di logging.

#### GUI
Per la GUI è necessario importare le dipendenze di javafx con i parametri `[--module-path]` e `[--add-module]`:

```
java --module-path "javafx-sdk-11.0.2/lib" --add-modules javafx.controls,javafx.fxml -jar client.jar gui [-i ip] [-p port] [-log]
```
dove `ip`, `port` e `log` sono equivalenti ai parametri della cli. Nel caso non si abbia l'SDK di javaFx si può trovare il link per scaricarlo alla voce dipendenze.

# Dipendenze
I jar sono stati compilati con SDK 13 (versione 13.0.2 di java), il programma per eseguire correttamente necessita di una versione pari o superiore a Java 11.

L'unica dipendendenza non presente nel JDK è javaFx. E' necessario [scaricarlo separatamente](https://openjfx.io/), si può posizionare in una qualsiasi directory e correggere il path passato in `--module-path` all'esecuzione della GUI .



