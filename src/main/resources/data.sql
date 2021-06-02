INSERT INTO role(id, name)
VALUES (1, 'ROLE_CLIENT'),
       (2, 'ROLE_MANAGER'),
       (3, 'ROLE_ADMIN'),
       (4, 'ROLE_OWNER');

INSERT INTO person(id, first_name, last_name, patronymic, email, password, passport_number_and_series)
VALUES (1, 'Alex', 'Cord', null, 'cord.alex@gmail.com',
        '$2y$12$JbPo1bdZsKJ.mFY8G7tOHuHBGW17nqVqbOo8RwZbh.h1dFIR6cIEu', '8655 898006'),
       (2, 'Mike', 'Birdperson', 'Rickovich',
        'person.and.bird.and.more@gmail.com', '$2y$12$GcEI/UKzmZGJ/jkUtlyRKeP.FO24ur1ZtIZ3jxlwu94c6tTeeeBoO', '8655 898007'),
       (3, 'Bob', 'Stone', null, 'stobob@yandex.ru',
        '$2y$12$InDYUCXK7h80d1yiEX0FUeH7xM1lz2d86YQ83x0cj0aAVRX.NzthK', '8655 898008'),
       (4, 'Savely', 'Litvinov', 'Igorevich', 'saliig@yandex.ru',
        '$2y$12$O4Z1lZqGjgG3gZsUZWyhAOh.RgQFXSljKQ82eYKm/HMHuRdT9e5su', '8655 898009'),
       (5, 'Maxim', 'Filippov', 'Vladimirovich', 'mafivl@yahoo.com',
        '$2y$12$RpCWvU1iwWxBFtLoAIXON.zlWRDn7iLsZL.ghfItKiDkDqU92YiIm', '8655 898010'),
       (6, 'Yulia', 'Egorova', 'Ivanovna', 'yuegiv@gmail.com',
        '$2y$12$VY0IQA3clkSF5K4yokzaiuqoxgqF74mdBsGsoNiTNGd4Dlyha.2Jq', '8655 898011'),
       (7, 'Nikolay', 'Melnikov', 'Egorovich', 'nimeeg@yandex.ru',
        '$2y$12$Si8qW/cI0CX9QhX6ZYoeMuZ9NzoEUgyd9kbRKXdJt6nfy/.KlGLV2', '8655 898012'),
       (8, 'Maria', 'Polyakova', 'Timofeevna', 'mapoti@mail.ru',
        '$2y$12$tlH.3hl8beffXEq1IpjEyumxYrlKtWIpE3tZSy5qQGinQYanmrVEK', '8655 898013'),
       (9, 'Ksenia', 'Popova', 'Mikhailovna', 'poksmi@mail.ru',
        '$2y$12$lsNX4zLcOpmGLcU6s1Wk2uOthk46c7D87PBQcARCLR5sdcbcrr25u', '8655 898014'),
       (10, 'Adam', 'Ponomarev', 'Demidovich', 'adpode@mail.ru',
        '$2y$12$KrSTkjVe1ZQYnU/4T8rcBu91YQ4UUa/lZiWL2nz8BAFCQLoidKShK', '8655 898015'),
       (11, 'Senior', 'Kohan', null, 'kohan@gmail.com',
        '$2y$12$nWylY3QtINR.k1DnYqrVT.jP2O56SnK.0gyji7SO.l0M4IXQxqRHC', '8655 898016');

INSERT INTO person_role(id, person_id, role_id)
VALUES (1, 1, 1),
       (2, 2, 1),
       (3, 2, 2),
       (4, 3, 1),
       (5, 3, 2),
       (6, 3, 3),
       (7, 3, 4),
       (8, 4, 1),
       (9, 5, 1),
       (10, 6, 1),
       (11, 7, 1),
       (12, 8, 1),
       (13, 9, 1),
       (14, 10, 1),
       (15, 11, 3);

INSERT INTO loan(id, min_amount, max_amount, min_duration_months, max_duration_months, interest_rate)
VALUES (1, 15000, 35000, 3, 30, 21),
       (2, 80000, 100000, 3, 30, 16),
       (3, 15000, 45000, 3, 30, 20);

INSERT INTO loan_case(id, client_id, loan_id, amount, duration_months, status_bank_side, status_client_side,
                      confirmation_date, closed)
VALUES (1, 1, 1, 23000, 3, 'APPROVED', 'APPROVED', '2020-11-04', true),
       (2, 2, 1, 20000, 3, 'APPROVED', 'APPROVED', '2020-03-18', false),
       (3, 3, 2, 100000, 6, 'APPROVED', 'PENDING', null, false),
       (4, 4, 2, 85000, 9, 'APPROVED', 'PENDING', null, false),
       (5, 4, 3, 40000, 3, 'PENDING', 'APPROVED', null, false);

INSERT INTO payment(id, loan_case_id, order_number, date, amount, loan_repayment_amount, interest_repayment_amount, paid_out)
VALUES (1, 1, 1, '2020-04-04', 7936.55, 7534.05, 402.50, true),
       (2, 1, 2, '2020-05-04', 7936.55, 7665.90, 270.65, true),
       (3, 1, 3, '2020-06-04', 7936.55, 7800.05, 136.50, true),
       (4, 2, 1, '2020-03-11', 6901.35, 6551.35, 350.00, true),
       (5, 2, 2, '2020-04-11', 6901.35, 6666.00, 235.35, true),
       (6, 2, 3, '2020-05-11', 6901.35, 6782.65, 118.70, false),
       (7, 3, 1, null, 17453.03, 16119.70, 1333.33, false),
       (8, 3, 2, null, 17453.03, 16334.62, 1118.40, false),
       (9, 3, 3, null, 17453.03, 16552.42, 900.61, false),
       (10, 3, 4, null, 17453.03, 16773.12, 679.91, false),
       (11, 3, 5, null, 17453.03, 16996.76, 456.27, false),
       (12, 3, 6, null, 17453.03, 17223.38, 229.65, false),
       (13, 4, 1, null, 10085.19, 8951.86, 1133.33, false),
       (14, 4, 2, null, 10085.19, 9071.22, 1013.98, false),
       (15, 4, 3, null, 10085.19, 9192.17, 893.03, false),
       (16, 4, 4, null, 10085.19, 9314.73, 770.46, false),
       (17, 4, 5, null, 10085.19, 9438.92, 646.27, false),
       (18, 4, 6, null, 10085.19, 9564.78, 520.41, false),
       (19, 4, 7, null, 10085.19, 9692.31, 392.88, false),
       (20, 4, 8, null, 10085.19, 9821.54, 263.65, false),
       (21, 4, 9, null, 10085.19, 9952.49, 132.7, false),
       (22, 5, 1, null, 13780.23, 13113.56, 666.67, false),
       (23, 5, 2, null, 13780.23, 13332.12, 448.11, false),
       (24, 5, 3, null, 13780.23, 13554.32, 225.91, false);
