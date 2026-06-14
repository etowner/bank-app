// @ts-check
import js from "@eslint/js";
import globals from "globals";
import tseslint from "typescript-eslint";
import reactPlugin from "eslint-plugin-react";
import reactHooks from "eslint-plugin-react-hooks";
import reactRefresh from 'eslint-plugin-react-refresh';
import { defineConfig, globalIgnores } from "eslint/config";

export default defineConfig([
  globalIgnores( ['node_modules/', 'dist/', 'build/', 'coverage/', '*.min.js', 'src/tests/', 'vite.config.*',]),

  // Baseline 
  {
    files: ['**/*.{ts,tsx,js,jsx}'],
    extends: [ js.configs.recommended,],
    languageOptions: { globals: globals.browser,},
  },

  // TypeScript
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      tseslint.configs.recommendedTypeChecked,
      tseslint.configs.stylisticTypeChecked,
    ],
    languageOptions: {
      parserOptions: {
        projectService: true,
        tsconfigRootDir: import.meta.dirname,
      },
    },
    rules: {
      '@typescript-eslint/no-explicit-any': 'warn',
      '@typescript-eslint/no-unused-vars': ['warn', { argsIgnorePattern: '^_' }],
      '@typescript-eslint/consistent-type-imports': 'warn',
    },
  },

  // React + Vite
  {
    files: ['**/*.{jsx,tsx}'],
    // plugins: { 'react-refresh': reactRefresh },
    extends: [
      reactPlugin.configs.flat.recommended,
      reactPlugin.configs.flat['jsx-runtime'], // React 17+ 
      reactRefresh.configs.recommended,
    ],
    settings: { react: { version: '19.0' }, },
    rules: {
      'react/prop-types': 'off', // TypeScript handles this
      // 'react-refresh/only-export-components': ['warn', { allowConstantExport: true }],
    },
  },
  reactHooks.configs.flat.recommended,
  
  {
    files: ['*.js', '*.mjs', 'vite.config.*'],
    extends: [tseslint.configs.disableTypeChecked],
  },


]);
