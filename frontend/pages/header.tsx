import { Avatar, Button, Dropdown, Navbar, Text } from "@nextui-org/react";
import { ShoppingCartIcon, UserIcon } from "@heroicons/react/24/solid";
import Link from "next/link";

export default function Header() {
    return (
        <Navbar variant="floating" isBordered>
            <Navbar.Brand>
                <Link href="/">
                    <Text weight="bold">Shoppping</Text>
                </Link>
            </Navbar.Brand>
            <Navbar.Content>
                <Navbar.Link>
                    <Button as={Link} href="/cart" auto css={{ padding: "$4" }} light>
                        <ShoppingCartIcon width={24} />
                    </Button>
                </Navbar.Link>
                <Navbar.Item>
                    <Dropdown>
                        <Dropdown.Trigger>
                            <Avatar squared icon={<UserIcon width={24} />} />
                        </Dropdown.Trigger>
                        <Dropdown.Menu>
                            <Dropdown.Item key="Profile">Profile</Dropdown.Item>
                            <Dropdown.Item key="Orders">Orders</Dropdown.Item>
                            <Dropdown.Section>
                                <Dropdown.Item key="Settings">Settings</Dropdown.Item>
                                <Dropdown.Item key="Logout" color="error">
                                    Logout
                                </Dropdown.Item>
                            </Dropdown.Section>
                        </Dropdown.Menu>
                    </Dropdown>
                </Navbar.Item>
            </Navbar.Content>
        </Navbar>
    );
}
