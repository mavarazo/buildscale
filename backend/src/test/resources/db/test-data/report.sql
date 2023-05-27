insert into bes_report (created,
                        duration,
                        project,
                        hostname,
                        task_exec_req,
                        status,
                        oid)
values ('2023-03-22T21:58:42.375501',
        300,
        'buildscale-sample',
        'localhost',
        'build',
        'FAILED',
        '07066981-ca78-46a7-bcd2-7e99f3d6ac23');

insert into bes_tag (key_val, report_oid, value_val, oid)
values ('gradle.version',
        '07066981-ca78-46a7-bcd2-7e99f3d6ac23',
        '8.0.2',
        '01b10041-07c0-4d3b-92be-ba04cd57ad02');

insert into bes_task (duration, endtime, incremental, messages, path, report_oid, starttime, status, oid)
values (42,
        1679600427195,
        false,
        'NO-SOURCE',
        ':bar:compileJava',
        '07066981-ca78-46a7-bcd2-7e99f3d6ac23',
        1679600427153,
        'SKIPPED',
        '5e4573dd-bd36-4e3e-8501-4a71e514d1a1');

insert into bes_task (duration, endtime, incremental, messages, path, report_oid, starttime, status, oid)
values (764,
        1679600428082,
        false,
        null,
        ':bar:compileTestJava',
        '07066981-ca78-46a7-bcd2-7e99f3d6ac23',
        1679600427318,
        'FAILED',
        'e1a84d84-1018-4bbe-8be5-60c8b9e963ae');

insert into bes_test (class_name, duration, name, report_oid, status, oid)
values ('BarTest',
        20,
        'bingo()',
        '07066981-ca78-46a7-bcd2-7e99f3d6ac23',
        'FAILED',
        '2aeaf54c-680a-48cb-a4e5-74584f0a63c8');

insert into bes_test_failure (message, stacktrace, test_oid, oid)
values ('expected: not <null>',
        'org.opentest4j.AssertionFailedError: expected: not <null>',
        '2aeaf54c-680a-48cb-a4e5-74584f0a63c8',
        'ed72e860-7b3b-48f9-960d-62e18b0c0a37');
