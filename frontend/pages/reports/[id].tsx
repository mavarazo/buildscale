import useSWR from 'swr'
import {useRouter} from "next/router";
import {format} from 'date-fns';
import {Report} from "@/lib/types";
import {Box, Card, CardHeader, Heading, SimpleGrid, Text} from "@chakra-ui/react";
import React from "react";
import TaskTable from "@/components/TaskTable";
import TestTable from "@/components/TestTable";
import Timeline from "@/components/Timeline";

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


export default function ReportPage() {
    const {query, isReady} = useRouter();
    const id = query.id;

    const fetcher = (...args) => fetch(...args).then(res => res.json())

    const {data, error} = useSWR<Report>(id ? `/api/reports/${id}` : null, fetcher)

    if (error) return <div>failed to load</div>
    if (!data) return <div>loading...</div>

    const translate = (key: string, ...args) => dict[key] ? dict[key] : key;

    return (
        <main>
            <Card>
                <CardHeader>
                    <Heading size="md">
                        <Text color="purple.500" as="span">{data.project}</Text>
                        {" "}on{" "}
                        <Text color="pink.500"  as="span">{data.hostname}</Text>
                        {" "}at{" "}
                        <Text color="green.500" as="span">{format(new Date(data.created), 'dd.MM.yyyy HH:mm:ss')}</Text>
                    </Heading>
                </CardHeader>
                <SimpleGrid mx={6} mb={6} columns={{sm: 2, md: 5}} spacing={3}>
                    {data.tags.sort((a: any, b: any) => {
                        return a.key!.localeCompare(b.key!)
                    }).map((tag, itemIndex) => (
                        <Box>
                            {translate(tag.key)}: {tag.value}
                        </Box>
                    ))}
                </SimpleGrid>
            </Card>

            <Timeline tasks={data.tasks} />
            <TaskTable tasks={data.tasks} durationInMillis={data.durationInMillis}/>
            <TestTable tests={data.tests}/>
        </main>
    );
}
