import { AppProps } from "next/app";
import "../styles/globals.css";

import "@/styles/globals.css";
import { Component } from "react";
import { Box, ChakraProvider, Flex, Heading, Text } from "@chakra-ui/react";
import Link from "next/link";
import { TimeIcon } from "@chakra-ui/icons";
import Image from "next/image";

export default function MyApp({ Component, pageProps }: AppProps) {
  return (
    <ChakraProvider>
      <Flex bg="gray.50">
        <Box w="full" mb={3} mx="auto" pl={6} py={6}>
          <Heading size="lg" fontWeight="semibold">
            <Link href="/">
              <TimeIcon /> Buildscale
            </Link>
          </Heading>
        </Box>
      </Flex>
      <Box bg="gray.50" px={6} py={6}>
        <Component {...pageProps} />
      </Box>
      <Flex
        pl={6}
        py={6}
        bg="gray.50"
        // flexDirection="row"
        // alignItems="start"
        // justifyContent="start"
      >
        <Box mb={3} mx="auto">
          <Text>Made with ❤️ by Marco Niederberger</Text>
        </Box>
        <Box mb={3} mx="auto">
          <Link href="https://github.com/mavarazo/buildscale" target="_blank">
            <Image src="/github-mark.svg" height={32} width={32}></Image>
          </Link>
        </Box>
      </Flex>
    </ChakraProvider>
  );
}
