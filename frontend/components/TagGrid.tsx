import {
  Card,
  Flex,
  SimpleGrid,
  Stat,
  StatLabel,
  StatNumber,
} from "@chakra-ui/react";
import React from "react";

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
  "os.version": "OS version",
};

const TagGrid = ({ tags }) => {
  const translate = (key: string, ...args) => (dict[key] ? dict[key] : key);

  return (
    <Flex flexDirection="column">
      <SimpleGrid columns={{ sm: 2, md: 4, xl: 5 }} p={3} spacing="24px">
        {tags
          .sort((a: any, b: any) => {
            return a.key!.localeCompare(b.key!);
          })
          .map((tag, itemIndex) => (
            <Card key={`box-tag-${itemIndex}`} p={3}>
              <Flex
                flexDirection="row"
                align="center"
                justify="center"
                w="100%"
              >
                <Stat me="auto">
                  <StatLabel
                    fontSize="sm"
                    color="gray.400"
                    fontWeight="bold"
                    pb=".1rem"
                  >
                    {translate(tag.key)}
                  </StatLabel>
                  <Flex>
                    <StatNumber fontSize="lg">{tag.value}</StatNumber>
                  </Flex>
                </Stat>
              </Flex>
            </Card>
          ))}
      </SimpleGrid>
    </Flex>
  );
};

export default TagGrid;
