# KOBI
I was on sick leave due to COVID-19 and used my free time to work on a project that had been on my mind for a while. As a security shift leader, I noticed that many security guards were frustrated with the late and error-prone shift assignments.

I came up with the idea of creating a software that would make it easy for security guards to submit their work constraints through a Google Form, and then automatically assign shifts based on the guards' training and constraints.

I implemented this idea using the Java programming language and an OOP method, with a JavaFX interface. The software reads an Excel file that imports data from Google Sheets and processes it using queries, and also loads a text file containing information about each employee's training.

It also keeps track of which guards have not yet submitted their constraints and sends them reminders. The placement algorithm works by considering each shift slot separately and creating a list of eligible guards for each shift. The shift with the shortest list is selected first, and the first guard on that list is assigned to that shift. The guard is then removed from the lists of other shifts they are not available for, and the process is repeated until all shifts are assigned. 

I also planned to add additional features such as balancing the number of shifts for each guard, manual placement, and automatic connection to Google Sheets via an API. However, the project was not completed due to the need to catch up on academic assignments and exams. I plan to continue working on it after exams and propose its use to my manager. 
Currently, the project is working in a basic way, but the recursion code and additional features have not been implemented yet.
