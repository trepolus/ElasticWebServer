# README

this Project was coded by

* Lucas Kummer
* Anna Zakipour

In this project we have used the methodology of pair programming, which is an agile software development technique where both of us have been working together on one workstation. 
The driver, the person writing the code has mainly been Lucas and the observer and navigator, the person reviewing each line of code has been Anna. 
Anna had mainly responsibility over the basic setup, client and server design and Lucas had responsibility over the main implementation, Client and Server implementation, incoming and outcoming workers, reading input data + creating output (statistics),  and to make the overall of the project to work.
## Setup

### Logger

In order to get this Project running you have to include all 3 jar files of the lib folder.
They are used for the LOGGER, a more sophisticated way of prining out lines in comparison to just "System.out.println".

It is really useful because you can track the thread number, the class it is called in and the timestamp, not just a printed info line.

## Guide

The Main class reads the request.txt in, sends requests line per line to the MasterNode(Mock-Server).
MasterNode distributes requests to Workers via workerqueue. If queue is empty, it waits until new WOrker arrives.
When Workers finish their job, the get put back into the queue.

The Client gets a response of masternode when the job is finished and adds a line to the output file.
Output file consists of WorkerID,Client_handled_id,Percentage_Of_Load, Percentage of Load is relative to the running time of the server (e.g. server runs 200ms, task needed 100ms --> 50%).
An overall statistic gets created at the end, putting out the load percentage and ms of Worker running.
It gets automatically created/overwritten in the folder "data".

The Logger puts all relevant (live)infos into the log, so you can track all activities in live time.
You will find a sample output and the sample log file in the folder "sample_output".
