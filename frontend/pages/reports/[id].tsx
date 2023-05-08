import useSWR from "swr";
import { useRouter } from "next/router";
import { format } from "date-fns";
import { Report } from "@/lib/types";
import { Card, CardHeader, Heading, Text } from "@chakra-ui/react";
import React from "react";
import TaskTable from "@/components/TaskTable";
import TestTable from "@/components/TestTable";
import Timeline from "@/components/Timeline";
import TagGrid from "@/components/TagGrid";

export default function ReportPage() {
  const { query, isReady } = useRouter();
  const id = query.id;

  const fetcher = (...args) => fetch(...args).then((res) => res.json());

  const { data, error } = useSWR<Report>(
    id ? `/api/reports/${id}` : null,
    fetcher
  );

  if (error) return <div>failed to load</div>;
  if (!data) return <div>loading...</div>;

  return (
    <main>
      <Card>
        <CardHeader>
          <Heading size="md">
            <Text color="purple.500" as="span">
              {data.project}
            </Text>{" "}
            on{" "}
            <Text color="pink.500" as="span">
              {data.hostname}
            </Text>{" "}
            at{" "}
            <Text color="green.500" as="span">
              {format(new Date(data.created), "dd.MM.yyyy HH:mm:ss")}
            </Text>
          </Heading>
        </CardHeader>
        <TagGrid tags={data.tags} />
      </Card>

      <Timeline tasks={data.tasks} />
      <TaskTable tasks={data.tasks} durationInMillis={data.durationInMillis} />
      <TestTable tests={data.tests} />
    </main>
  );
}
