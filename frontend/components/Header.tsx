import { Box, Flex, Heading, Spacer } from "@chakra-ui/react";
import Link from "next/link";
import { TimeIcon } from "@chakra-ui/icons";
import React from "react";

const Header = () => {
  return (
    <Flex bg="gray.50" minWidth="max-content" alignItems="center" gap={2}>
      <Box mb={3} mx="auto" pl={6} py={6}>
        <Heading size="lg" fontWeight="semibold">
          <Link className="inline-flex items-center" href="/">
            <TimeIcon mx={2} /> Buildscale
          </Link>
        </Heading>
      </Box>
      <Spacer />
    </Flex>
  );
};

export default Header;
