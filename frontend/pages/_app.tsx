import { AppProps } from "next/app";
import "../styles/globals.css";

import "@/styles/globals.css";
import { Component } from "react";
import { Box, ChakraProvider } from "@chakra-ui/react";
import Header from "@/components/Header";
import Footer from "@/components/Footer";

export default function MyApp({ Component, pageProps }: AppProps) {
  return (
    <ChakraProvider>
      <Header />
      <Box bg="gray.50" px={6} py={6}>
        <Component {...pageProps} />
      </Box>
      <Footer />
    </ChakraProvider>
  );
}
