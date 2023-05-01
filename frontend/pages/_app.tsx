import {AppProps} from 'next/app'
import '../styles/globals.css'

import '@/styles/globals.css';
import {Component, ReactNode} from "react";
import {ChakraProvider} from "@chakra-ui/react";

export default function MyApp({Component, pageProps}: AppProps) {
    return (
        <ChakraProvider>
            <Component {...pageProps} />
        </ChakraProvider>
    );
}