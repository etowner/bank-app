import React from "react";
import "chart.js/auto";
import { Pie } from "react-chartjs-2";

export default function PieChart({ accounts }) {
  const totalBalance = accounts.reduce(
    (sum, account) => sum + account.balance,
    0
  );

  function getRandomColor() {
    const r = Math.floor(Math.random() * 256);
    const g = Math.floor(Math.random() * 256);
    const b = Math.floor(Math.random() * 256);
    return `rgb(${r}, ${g}, ${b})`;
  }

  const data = {
    labels: accounts.map((account) => `${account.type} - ${account.accountID}`),
    datasets: [
      {
        label: "%",
        data: accounts.map((account) => (account.balance / totalBalance) * 100),
        backgroundColor: [
          "rgba(40, 44, 52, 1)",
          "rgba(239, 196, 92, 1)",
          "rgba(112, 128, 144, 1)",
          getRandomColor(), // Leads to an endless loop
        ],
        hoverOffset: 4,
      },
    ],
  };

  const options = {
    plugins: {
      legend: {
        display: true,
        position: "top",
      },
    },
    layout: {
      padding: 10,
      backgroundColor: "rgba(40, 44, 52, 1)",
    },
    elements: {
      backgroundColor: "rgba(40, 44, 52, 1)",
    },
  };

  return (
    <div>
      <Pie data={data} options={options} />
    </div>
  );
}
