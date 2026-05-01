/* eslint-disable react/prop-types */
import React from "react";
import "chart.js/auto";
import { Line } from "react-chartjs-2";

export default function LineChart({ accountID, transHistory }) {
  const transactionLabels = Object.values(transHistory).map(t => 
    new Date(t.timestamp).toLocaleDateString()
  );
  const transactionAmounts = Object.values(transHistory).map(t => t.amount);

  const data = {
    labels: transactionLabels,
    datasets: [{
      label: accountID,
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
