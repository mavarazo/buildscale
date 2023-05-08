/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  async rewrites() {
    return [
      {
        source: "/api/:path*",
        destination: "http://localhost:15431/v1/:path*",
      },
    ];
  },
};

module.exports = nextConfig;
