/* eslint-disable react/prop-types */
import React from "react";
import "chart.js/auto";
import { Line } from "react-chartjs-2";

export default function LineChart({ accountNumber, transactions }) {
  let transactionLabels = [];
  let transactionAmounts = [];
  if (transactions && Object.keys(transactions).length > 0) {
     transactionLabels = Object.values(transactions).map(t => 
      new Date(t.timestamp).toLocaleDateString()
    );
     transactionAmounts = Object.values(transactions).map(t => t.amount);
  }

  const data = {
    labels: transactionLabels,
    datasets: [{
      label: accountNumber,
      data: transactionAmounts,
      borderColor: "rgb(112, 128, 144)",
      backgroundColor: "rgba(112, 128, 144, 0.5)",
    }]
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        display: true,
        position: "top",
      },
    },

    title: {
      display: true,
      text: "Transaction graph",
    },
  };

  return (
    <div>
      <Line data={data} options={options} />
    </div>
  );
}
