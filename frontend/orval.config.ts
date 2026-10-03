import { defineConfig } from "orval";

export default defineConfig({
  apiSchemas: {
    input: "http://localhost:8080/v3/api-docs",
    output: {
      target: "src/types/schemas.zod.ts",
      client: "zod",
    },
  },
});