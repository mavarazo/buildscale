import useSWR from "swr";
import { useRouter } from "next/router";
import { Test } from "@/lib/types";
import {
  Card,
  CardBody,
  CardHeader,
  Code,
  Heading,
  Text,
  VStack,
} from "@chakra-ui/react";
import React from "react";

export default function TestPage() {
  const { query, isReady } = useRouter();
  const id = query.id;

  const { data, error } = useSWR<Test>(
    id ? `/api/v1/tests/${id}` : null,
    (url: string) => fetch(url).then((res) => res.json())
  );

  if (error) return <div>failed to load</div>;
  if (!data) return <div>loading...</div>;

  return (
    <main>
      <Card mt={6}>
        <CardHeader>
          <Heading size="md">Test details</Heading>
          <Text fontSize="sm" color="gray.500" fontWeight="normal">
            <Text fontWeight="bold" as="span">
              {data.name}
            </Text>
            {" in "}
            <Text fontWeight="bold" as="span">
              {data.className}
            </Text>
            {" is "}
            <Text fontWeight="bold" as="span">
              {data.status}
            </Text>
          </Text>
        </CardHeader>
        <CardBody>
          {data.failures.map((failure, itemIndex) => {
            return (
              <VStack
                key={`stack-failure-${itemIndex}`}
                spacing={2}
                alignItems="flex-start"
              >
                <Heading size="sm">Message</Heading>
                <Code w="full">{failure.message}</Code>
                <Heading size="sm">Stacktrace</Heading>
                <Code w="full">{failure.stacktrace}</Code>
              </VStack>
            );
          })}
        </CardBody>
      </Card>
    </main>
  );
}
