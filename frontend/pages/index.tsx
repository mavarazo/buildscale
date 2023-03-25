import Head from 'next/head'
import Image from 'next/image'
import { Inter } from 'next/font/google'
import styles from '@/styles/Home.module.css'

export default function Posts({ reports }) {
  return (
      <div>
        <h1>My latest posts {reports}</h1>
        {/*{posts.map((post) => (*/}
        {/*    <h2>{post.title}</h2>*/}
        {/*))}*/}
      </div>
  );
}

export async function getStaticProps() {
    // const response = await fetch(`http://localhost:1337/categories?slug=${params.slug}`)
    // const data = await res.json()

  return {
    props: {
      posts: 'await fetchPosts()',
    }
  };
}