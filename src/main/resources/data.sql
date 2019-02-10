INSERT INTO user (username, password, name) VALUES
	('ante', '$2a$11$RCnWSP2XsN8LRKEs5Ru/3Owx3UL9syVD8ESRgb1Osrt2Eb95Ccgk6', 'Ante Antic'),
	('mate', '$2a$11$lzhoOwelPtZ58jYpi4Rzp.k0TIHqauGnAGN1BzKdt2bqYWSDlYGEe', 'Mate Matic'),
	('ana', '$2a$11$v5BM/TefyBA.fd/xDX/gvef8OtG3t.mxMxFUGbaiUfsvOtiDAzNzK', 'Ana Anic');
INSERT INTO surveys (survey_name, description, user, is_active) VALUES
  ('Anketa o studentima predmeta', 'Anketa koja ispituje osnovne podatke studenata na predmetu', 1, true),
  ('Kvaliteta predavanja', 'Ispituje se zadovoljstvo studenata s kvalitetom predavanja', 1, false),
  ('Kvaliteta laboratorijskih vjezbi', 'Ispituje se kvaliteta labosa', 2, false),
  ('Tezina predmeta RASUS', 'Koliko je studentima zahtjevan predmet RASUS', 1, true),
  ('Organiziranost predmeta Objektno oblikovanje', 'Zadovoljstvo studenata sa nacinom organizacije predmeta OO',
   2, true);
INSERT INTO question (question_text, survey_id) VALUES
  ('Koliko dugo studirate FER?', 1),
  ('Idete li inace na predavanja?', 1),
  ('Kako bi ocjenili predavanja?', 2),
  ('Kako bi ocjenili laboratorijske vjezbe?', 3),
  ('Koliko vam je tezak RASUS?', 4),
  ('Kako bi ocjenili organiziranost predmeta?', 5),
  ('Jest zadovoljni sa studijem?', 1),
  ('Koliko cesto imate suicidalne misli?', 1);
INSERT INTO answer (answer_text, votes, question_id) VALUES
  ('3 godine', 8, 1),
  ('4 godine', 14, 1),
  ('Vise od 5 godina', 2, 1),
  ('Prakticki nikad', 12, 2),
  ('Ponekad', 2, 2),
  ('Prakticki uvijek', 0, 2),
  ('Izvrsna', 0, 3),
  ('Nije lose, ali moze bolje', 0, 3),
  ('Katastrofa', 0, 3),
  ('Izvrsna', 0, 4),
  ('Nije lose, ali moze bolje', 0, 4),
  ('Katastrofa', 0, 4),
  ('Jako tezak', 0, 5),
  ('Osrednje tezine', 0, 5),
  ('Lagan', 0, 5),
  ('E moj Zvone', 0, 6),
  ('Nije lose, ali moze bolje', 0, 6),
  ('Odlicno', 0, 6),
  ('Ne', 20, 7),
  ('Nije lose, ali moze bolje', 2, 7),
  ('Da', 2, 7),
  ('Nikad', 18, 8),
  ('Svaki dan', 2, 8),
  ('Konstantno', 4, 8);

