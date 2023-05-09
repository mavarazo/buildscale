import { Flex, Text } from "@chakra-ui/react";
import Link from "next/link";
import { FaGithub } from "react-icons/all";

const Footer = () => {
  return (
    <Flex
      bg="gray.50"
      minWidth="max-content"
      alignItems="center"
      gap={2}
      direction="column"
    >
      <Text>Proudly made in 🇨🇭 by Marco Niederberger</Text>
      <Flex mb={3} mx="auto" pl={6} py={6}>
        <Link href="https://github.com/mavarazo/buildscale" target="_blank">
          <FaGithub />
        </Link>
      </Flex>
    </Flex>
  );
};

export default Footer;
