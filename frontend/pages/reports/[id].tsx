import useSWR from 'swr'
import {useRouter} from "next/router";
import ms from 'ms';
import {format} from 'date-fns';
import Timeline from "@/components/Timeline";
import {Report} from "@/lib/types";
import {Box, Heading, SimpleGrid, Text} from "@chakra-ui/react";
import React from "react";
import TaskTable from "@/components/TaskTable";
import TestTable from "@/components/TestTable";

const dict: Record<string, string> = {
    "gradle.version": "Gradle version",
    "java.cpu": "CPU",
    "java.memory.free": "JVM memory free",
    "java.memory.max": "JVM memory max",
    "java.memory.total": "JVM memory total",
    "java.vendor": "Java vendor",
    "java.version": "Java version",
    "os.arch": "OS architecture",
    "os.name": "OS name",
    "os.version": "OS version"
}

const fetcher = (...args) => fetch(...args).then(res => res.json())

export default function ReportPage() {
    const {query, isReady} = useRouter();
    const id = query.id;

    const {data, error} = useSWR<Report>(id ? `/api/reports/${id}` : null, fetcher)

    if (error) return <div>failed to load</div>
    if (!data) return <div>loading...</div>

    const translate = (key: string, ...args) => dict[key] ? dict[key] : key;

    return (
        <main>
            <Heading>
                <span className="text-purple-500">{data.project}</span>
                &nbsp;on&nbsp;
                <span className="text-pink-500">{data.hostname}</span>
                &nbsp;at&nbsp;
                <span className="text-green-500">{format(new Date(data.created), 'dd.MM.yyyy HH:mm:ss')}</span>
            </Heading>

            <SimpleGrid columns={{sm: 2, md: 5}} spacing={3}>
                {data.tags.sort((a: any, b: any) => {
                    return a.key!.localeCompare(b.key!)
                }).map((tag, itemIndex) => (
                    <Box>
                        {translate(tag.key)}: {tag.value}
                    </Box>
                ))}
            </SimpleGrid>

            <Timeline tasks={data.tasks}/>
            <TaskTable tasks={data.tasks} durationInMillis={data.durationInMillis}/>
            <TestTable tests={data.tests}/>
        </main>
    );
}
